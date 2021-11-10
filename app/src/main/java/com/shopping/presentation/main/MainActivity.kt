package com.shopping.presentation.main

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.PanelState
import com.discord.panels.PanelsChildGestureRegionObserver
import com.shopping.databinding.ActivityMainBinding
import com.shopping.presentation.main.MainActivityEvents.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), PanelsChildGestureRegionObserver.GestureRegionsListener {
    private var binding: ActivityMainBinding? = null
    private val viewModel: MainActivityViewModel by viewModel()
    private val shopListId by lazy { intent.getIntExtra("shopListId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() = binding?.apply {
        viewModel.on(CLOSE_PANELS) { overlappingPanels.closePanels() }
        viewModel.on(SHOW_SHOP_LIST_ITEMS) { it: Int ->
            supportFragmentManager.setFragmentResult("shopListChanged", bundleOf("id" to it))
            supportFragmentManager.setFragmentResult("shopListItemsChanged", bundleOf("id" to it))
        }
        viewModel.start(shopListId)

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
                viewModel.changeShowShopList(it.getInt("id"))
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
