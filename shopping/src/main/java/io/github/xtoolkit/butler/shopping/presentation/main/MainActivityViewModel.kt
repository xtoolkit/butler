package io.github.xtoolkit.butler.shopping.presentation.main

import androidx.lifecycle.viewModelScope
import com.discord.panels.PanelState
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.core.interactor.AddShopListUC
import io.github.xtoolkit.butler.shopping.core.interactor.GetAllShopListUC
import io.github.xtoolkit.butler.shopping.core.interactor.GetShopListUC
import io.github.xtoolkit.butler.shopping.presentation.main.MainActivityEvents.CLOSE_PANELS
import io.github.xtoolkit.butler.shopping.presentation.main.MainActivityEvents.SHOW_SHOP_LIST_ITEMS
import io.github.xtoolkit.butler.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getAllShopListUC: GetAllShopListUC,
    private val getShopListUC: GetShopListUC,
    private val addShopListUC: AddShopListUC
) : BaseViewModel<MainActivityEvents>() {
    private var lastShopListId: Int = -1
    val lastPanelsSate =
        MutableStateFlow(listOf<PanelState>(PanelState.Closed, PanelState.Closed))

    fun panelStateChange(panel: Int, state: PanelState) = viewModelScope.launch(Dispatchers.Main) {
        val last = lastPanelsSate.value
        if (last[panel] != state) {
            val lastList = last.toMutableList()
            lastList[panel] = state
            lastPanelsSate.emit(lastList)
        }
    }

    fun changeShowShopList(newId: Int) {
        if (newId != lastShopListId) {
            lastShopListId = newId
            trigger(SHOW_SHOP_LIST_ITEMS, newId)
        }
        trigger(CLOSE_PANELS)
    }

    fun start(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (lastShopListId != -1 && id != -1) return@launch
        getAllShopListUC(Unit)
            .onSuccess {
                val list = it.first()
                if (list.isEmpty()) addShopListUC(ShopList(0, "Default list"))
                    .onSuccess { x -> if (id == -1) changeShowShopList(x.id) }
                else if (id == -1) changeShowShopList(list[0].id)
            }
        if (id != -1) getShopListUC(ShopList(id, "")).onSuccess { changeShowShopList(it.id) }
    }
}