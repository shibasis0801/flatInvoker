package dev.shibasis.tester.react;

import android.app.Application;
import com.facebook.react.*
import com.facebook.soloader.SoLoader;
import dev.shibasis.flatinvoker.react.TurboKotlinPackage

class MainApplication: Application(), ReactApplication {
   private val rnHost = object: ReactNativeHost(this) {
           override fun getUseDeveloperSupport() = BuildConfig.DEBUG
           override fun getJSMainModuleName() = "index"

           override fun getPackages() = PackageList(this).packages.apply {
               add(TurboKotlinPackage())
           }
       };

  override fun getReactNativeHost() = rnHost


  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false);
  }
}
