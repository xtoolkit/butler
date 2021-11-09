package com.shopping.presentation.main

import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
    private val shoplistId by lazy { intent.getIntExtra("shoplistId", -1) }
    private val isRTL by lazy { resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL }
    private val isLandscape by lazy { resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    private var isNavigationInRight = false

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
        viewModel.start(shoplistId)

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

        var statusBarHeight: Int? = null
        val outRect = Rect()
        (window.decorView as ViewGroup).getWindowVisibleDisplayFrame(outRect)
        isNavigationInRight = resources.displayMetrics.widthPixels == outRect.bottom

        resources.getIdentifier("status_bar_height", "dimen", "android").takeIf { it > 0 }?.let {
            statusBarHeight = resources.getDimensionPixelSize(it)
            if (isLandscape) setNotches(statusBarHeight!!)
        }

        if (!isLandscape) resources.getIdentifier("navigation_bar_height", "dimen", "android")
            .takeIf { it > 0 }?.let {
                setNotches(statusBarHeight!!, resources.getDimensionPixelSize(it))
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

    private fun setNotches(top: Int, bottom: Int = 0) = binding?.apply {
        startPanelFrame.setPadding(0, top, 0, bottom)
        centerPanelFrame.setPadding(0, top, 0, bottom)
        endPanelFrame.setPadding(0, top, 0, bottom)
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
