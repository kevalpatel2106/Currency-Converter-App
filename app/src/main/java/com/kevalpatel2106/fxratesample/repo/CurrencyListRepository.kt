package com.kevalpatel2106.fxratesample.repo

import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.kevalpatel2106.fxratesample.repo.network.CurrencyListDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val currencyListApi: CurrencyListApi
) {

    fun getCurrencyList(): Single<CurrencyListDto> {
        return currencyListApi.getListOfBaseCurrency()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
