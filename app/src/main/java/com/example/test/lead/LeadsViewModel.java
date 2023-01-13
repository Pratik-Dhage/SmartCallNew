package com.example.test.lead;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.Disposable;

public class LeadsViewModel extends ViewModel {


    private Disposable subscribtion; //Disposable Interface used to prevent observer from receiving items from Observer before all items are loaded.


    //Call to API with Required Function

 /*   subscribtion = (Disposable) Global.apiService().getAllProducts(WebServices.Domain2+WebServices.allProducts)
                  .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(
                          this::onHomeApiSuccess , this::onApiError
                          );

    private void onHomeApiSuccess(HomeResponse result) {
        mutHomeResponseApi.setValue(result);
    }

    private void onApiError(Throwable error) {
        mutErrorResponse.setValue(error.getLocalizedMessage());
    }
 */

}
