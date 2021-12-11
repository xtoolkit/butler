package io.github.xtoolkit.butler.shopping.presentation.main

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.PanelState
import com.discord.panels.PanelsChildGestureRegionObserver
import io.github.xtoolkit.butler.shopping.databinding.ActivityMainBinding
import io.github.xtoolkit.butler.shopping.presentation.main.MainActivityEvents.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), PanelsChildGestureRegionObserver.GestureRegionsListener {
    private var binding: ActivityMainBinding? = null
    private val viewModel: MainActivityViewModel by viewModel()
    private val setting by lazy { getSharedPreferences("mainActivitySetting", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() = binding?.apply {
        viewModel.on(CLOSE_PANELS) { overlappingPanels.closePanels() }
        viewModel.on(SHOW_SHOP_LIST_ITEMS) { it: Int ->
            supportFragmentManager.apply {
                setFragmentResult("shopListChanged", bundleOf("id" to it))
                setFragmentResult("shopListItemsChanged", bundleOf("id" to it))
                setFragmentResult("shopListManageChanged", bundleOf("id" to it))
            }
        }

        viewModel.start(setting.getInt("lastListSelected", -1))

        overlappingPanels.apply {
            registerStartPanelStateListeners(object : OverlappingPanelsLayout.PanelStateListener {
                override fun onPanelStateChange(panelState: PanelState) {
                    viewModel.panelStateChange(0, panelState)
                }
            })
            registerEndPanelStateListeners(object : OverlappingPanelsLayout.PanelStateListener {
                override fun onPanelStateChange(panelState: PanelState) {
                    viewModel.panelStateChange(1, panelState)
                }
            })
        }

        lifecycleScope.launchWhenResumed {
            viewModel.lastPanelsSate.collect {
                overlappingPanels.handleStartPanelState(it[0])
                overlappingPanels.handleEndPanelState(it[1])
                centerPanelBackground.visibility =
                    if (it[0] !is PanelState.Opened && it[1] !is PanelState.Opened) View.VISIBLE else View.INVISIBLE
            }
        }

        supportFragmentManager.apply {
            setFragmentResultListener("onSelectedShopListChange", this@MainActivity) { _, it ->
                val id = it.getInt("id")
                viewModel.changeShowShopList(id)
                setting.edit { putInt("lastListSelected", id) }
            }

            setFragmentResultListener("openShopListMenu", this@MainActivity) { _, _ ->
                overlappingPanels.openStartPanel()
            }

            setFragmentResultListener("onSelectThemeSwitch", this@MainActivity) { _, _ ->
                AppCompatDelegate.setDefaultNightMode(
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                        AppCompatDelegate.MODE_NIGHT_NO
                    else
                        AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            setFragmentResultListener("requestOpenShopListSetting", this@MainActivity) { _, _ ->
                overlappingPanels.openEndPanel()
            }

            setFragmentResultListener("requestLockPanel", this@MainActivity) { _, it ->
                val state =
                    if (it.getBoolean("lock")) OverlappingPanelsLayout.LockState.CLOSE else OverlappingPanelsLayout.LockState.UNLOCKED
                overlappingPanels.setEndPanelLockState(state)
                overlappingPanels.setStartPanelLockState(state)
            }
        }
    }

    override fun onGestureRegionsUpdate(gestureRegions: List<Rect>) {
        binding!!.overlappingPanels.setChildGestureRegions(gestureRegions)
    }

    override fun onResume() {
        super.onResume()
        PanelsChildGestureRegionObserver.Provider.get().addGestureRegionsUpdateListener(this)
    }

    override fun onPause() {
        super.onPause()
        PanelsChildGestureRegionObserver.Provider.get().removeGestureRegionsUpdateListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }
}
