package com.umesh.cafehaze.di

import com.umesh.cafehaze.model.repository.FavoriteRepository
import com.umesh.cafehaze.model.repository.OrderRepository
import com.umesh.cafehaze.model.repository.MenuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://allaqplhgigeyiigmhhd.supabase.co",
            supabaseKey = "sb_publishable_ccnGEBgXnZnxKj0Pr8HaZg_D17Ir12b"
        ) {
            install(Postgrest)
            install(Auth)
            install(Realtime)
        }
    }


    @Provides
    @Singleton
    fun provideMenuRepository(client: SupabaseClient): MenuRepository {
        return MenuRepository(client)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(client: SupabaseClient): FavoriteRepository {
        return FavoriteRepository(client)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(client: SupabaseClient): OrderRepository {
        return OrderRepository(client)
    }
}