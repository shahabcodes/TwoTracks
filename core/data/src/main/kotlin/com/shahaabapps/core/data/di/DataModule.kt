package com.shahaabapps.core.data.di

import com.shahaabapps.core.data.learning.FirebaseLearningRepository
import com.shahaabapps.core.data.learning.LearningRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.shahaabapps.core.data.BuildConfig
import com.shahaabapps.core.data.auth.AuthRepository
import com.shahaabapps.core.data.auth.FirebaseAuthRepository
import com.shahaabapps.core.data.couple.CoupleRepository
import com.shahaabapps.core.data.couple.FirebaseCoupleRepository
import com.shahaabapps.core.data.files.FirebaseModuleFilesRepository
import com.shahaabapps.core.data.ink.FirebaseInkNotesRepository
import com.shahaabapps.core.data.ink.InkNotesRepository
import com.shahaabapps.core.data.files.ModuleFilesRepository
import com.shahaabapps.core.data.study.FirebaseStudyRepository
import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.tracking.FirebaseTrackingRepository
import com.shahaabapps.core.data.tracking.TrackingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth.apply {
        if (BuildConfig.USE_FIREBASE_EMULATOR) useEmulator(BuildConfig.FIREBASE_EMULATOR_HOST, 9099)
    }

    /** Firestore with a persistent offline cache: both of you work offline and sync later. */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore.apply {
        if (BuildConfig.USE_FIREBASE_EMULATOR) useEmulator(BuildConfig.FIREBASE_EMULATOR_HOST, 8080)
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()
    }

    /** Cloud Storage, for the files attached to a module. */
    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage = Firebase.storage.apply {
        if (BuildConfig.USE_FIREBASE_EMULATOR) useEmulator(BuildConfig.FIREBASE_EMULATOR_HOST, 9199)
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface DataBindsModule {
    @Binds
    @Singleton
    fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    fun bindCoupleRepository(impl: FirebaseCoupleRepository): CoupleRepository

    @Binds
    @Singleton
    fun bindStudyRepository(impl: FirebaseStudyRepository): StudyRepository

    @Binds
    @Singleton
    fun bindTrackingRepository(impl: FirebaseTrackingRepository): TrackingRepository

    @Binds
    @Singleton
    fun bindLearningRepository(impl: FirebaseLearningRepository): LearningRepository

    @Binds
    @Singleton
    fun bindModuleFilesRepository(impl: FirebaseModuleFilesRepository): ModuleFilesRepository

    @Binds
    @Singleton
    fun bindInkNotesRepository(impl: FirebaseInkNotesRepository): InkNotesRepository
}
