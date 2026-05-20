
package com.umesh.cafehaze.di

import com.umesh.cafehaze.payment.UpiRepository
import com.umesh.cafehaze.utils.qr.QrGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Provides
    @Singleton
    fun provideUpiRepository(): UpiRepository = UpiRepository()

    @Provides
    @Singleton
    fun provideQrGenerator(): QrGenerator = QrGenerator()
}