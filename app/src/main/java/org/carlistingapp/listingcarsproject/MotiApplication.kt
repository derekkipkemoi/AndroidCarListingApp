package org.carlistingapp.listingcarsproject

import android.app.Application
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModelFactory
import org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels.ListCarViewModelFactory
import org.carlistingapp.listingcarsproject.ui.home.postCar.viewModels.PostCarViewModelFactory
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.ImageResizer
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MotiApplication : Application() , KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MotiApplication))
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { SharedPrefManager(instance()) }
        bind() from singleton { ImageResizer() }
        bind() from singleton { ListingCarsAPI(instance()) }
        bind() from singleton { AuthRepository(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { UserViewModelFactory(instance()) }
        bind() from singleton { PostCarViewModelFactory(instance()) }
        bind() from singleton { ListCarViewModelFactory(instance()) }
        bind() from singleton { AuthViewModelFactory(instance()) }
    }
}