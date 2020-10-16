package org.carlistingapp.listingcarsproject.ui.home

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_home.*
import me.ibrahimsn.lib.SmoothBottomBar
import org.carlistingapp.listingcarsproject.BuildConfig
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.ActivityHomeBinding
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class HomeActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val session : SharedPrefManager by instance()
    private lateinit var binding : ActivityHomeBinding
    private lateinit var bottomMenu : SmoothBottomBar
    private lateinit var navController : NavController
    private lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("PackageManagerGetSignatures", "WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationInfo.targetSdkVersion = 14;
        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(this)
        //Get facebook keys
        // printKeyHash()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)


        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar.setContentInsetsAbsolute(0, 0)
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.search -> {
                    navController.navigate(R.id.searchCarFragment)
                    drawer_layout.closeDrawer(Gravity.START, false)
                }
                R.id.rate -> {
                    val uri: Uri = Uri.parse("market://details?id=$packageName")
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    try {
                        startActivity(goToMarket)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
                    }
                }
                R.id.share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Want to sell or buy a Vehicle. Get Moti Kenya http://play.google.com/store/apps/details?id=$packageName")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)


                }
                R.id.privacy_policy -> binding.root.snackBar("Updating Soon")
                R.id.terms_and_conditions -> binding.root.snackBar("Updating Soon")

            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_bottom_app_bar,menu)
        bottomBar.setupWithNavController(menu!!,navController)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),
            drawer_layout
        )

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 0.8f
        applyOverrideConfiguration(newOverride)
    }



//    @SuppressLint("PackageManagerGetSignatures")
//    private fun printKeyHash() {
//        // Add code to print out the key hash
//        try {
//            val info = packageManager.getPackageInfo(
//                "org.carlistingapp.listingcarsproject",
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//        } catch (e: NoSuchAlgorithmException) {
//        }
//    }
}