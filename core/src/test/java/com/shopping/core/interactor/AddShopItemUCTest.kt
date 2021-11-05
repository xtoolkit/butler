package com.shopping.core.interactor

import com.shopping.core.data.repositories.ShopItemRepo
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

//given
//when
//then
class AddShopItemUCTest {

    @Test
    fun givenShopItemRepoThrows_whenAddShopItemUCInvoked_thenFailureResultIsReturned() {
        //Arrange
        val shopItemRepoMock = mockk<ShopItemRepo>()
        val shopListMock = mockk<ShopList>()
        val shopItemMock = mockk<ShopItem>()
        val subject = AddShopItemUC(shopItemRepoMock)

        //Act
        val result = runBlocking { subject(shopListMock to shopItemMock) }

        //Assert
        coVerify(exactly = 1) { shopItemRepoMock.addShopItem(any(), any()) }

        confirmVerified(
            shopItemRepoMock,
            shopItemMock,
            shopItemMock
        )

        assert(result.isFailure)
    }

    @Test
    fun `given addShopItem returns mock when AddShopItemUC invoked then Success with mock returned`() {
        //Arrange
        val shopItemRepoMock = mockk<ShopItemRepo>()
        val shopListMock = mockk<ShopList>()
        val shopItemMock = mockk<ShopItem>()
        val mockResult = Result.success(mockk<ShopItem>())
        val subject = AddShopItemUC(shopItemRepoMock)

        coEvery { shopItemRepoMock.addShopItem(shopListMock, shopItemMock) } returns mockResult

        //Act
        val result = runBlocking { subject(shopListMock to shopItemMock) }

        //Assert
        coVerify(exactly = 1) { shopItemRepoMock.addShopItem(any(), any()) }

        confirmVerified(
            shopItemRepoMock,
            shopItemMock,
            shopItemMock
        )

        assert(mockResult == result)
    }
}