package org.carlistingapp.listingcarsproject.ui.auth.views

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.GoogleSignInAccessTokenDataClass
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentSignInUserBinding
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModel
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModelFactory
import org.carlistingapp.listingcarsproject.utils.CustomAlertDialog
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInUserFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val networkConnectionInterceptor : NetworkConnectionInterceptor by instance()
    val api : ListingCarsAPI by instance()
    val repository : AuthRepository by instance()
    val factory: AuthViewModelFactory by instance()
    val session : SharedPrefManager by instance()
    private lateinit var viewModel : AuthViewModel

    private lateinit var binding: FragmentSignInUserBinding
    private lateinit var callbackManager : CallbackManager
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this.requireActivity(),factory).get(AuthViewModel ::class.java)
        //Google sign In option
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(getString(R.string.web_client_id))
            .requestEmail()
            .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        //Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    if (loginResult != null){
                        val accessToken = loginResult.accessToken?.token
                        if (accessToken != null) {
                            binding.progressBar.visibility = View.VISIBLE
                            try {
                            viewModel.getSignedInWithFacebookAccount(accessToken)
                                viewModel.loggedInUseWithFacebookAccountResponse.observe(viewLifecycleOwner, Observer {
                                    binding.progressBar.visibility = View.INVISIBLE
                                    if (it.access_token != null){
                                        binding.root.snackBar(it.message)
                                        session.saveSession(it.userObject?.id)
                                        session.saveUserName(it.userObject?.facebook?.name!!)
                                        session.saveUserEmail(it.userObject.facebook.email!!)
                                        session.saveUserPhoneNumber(it.userObject.phoneNumber?.number!!)
                                        session.saveUserPhoneNumberVerified(it.userObject.phoneNumber.verified!!)
                                        session.saveUserSignInMethod(it.userObject.method!!)
                                        findNavController().popBackStack(R.id.signInUserFragment, true)
                                        findNavController().popBackStack(R.id.listCarFragment, true)

                                        if (it.userObject.phoneNumber.verified == false){
                                            loadVerifyPhoneFragment()
                                        }
                                        else{
                                            loadHomeActivity()
                                        }

                                    }

                                })
                            }catch (e : NoInternetException){
                               binding.root.snackBar(e.message)
                            }

                        }
                    }
                }
                override fun onCancel() {

                }
                override fun onError(exception: FacebookException) {
                    // App code
                    binding.googleSignIn.isEnabled = true
                    binding.facebookLogin.isEnabled = true
                    binding.loginButton.isEnabled = true
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.root.snackBar("Login with facebook failed!!")
                }
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_in_user, container, false)
        val view = binding.root
        binding.googleSignIn.setOnClickListener {
            googleSignIn();
        }
        binding.loginButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.loginUserWithPhoneEmailFragment)
        }
        binding.facebookLoginHide.fragment = this
        binding.facebookLogin.setOnClickListener{
            binding.facebookLoginHide.performClick()
            binding.progressBar.visibility = View.VISIBLE
            binding.googleSignIn.isEnabled = false
            binding.facebookLogin.isEnabled = false
            binding.loginButton.isEnabled = false
        }
        return view
    }

    private fun googleSignIn() {
        binding.progressBar.visibility = View.VISIBLE
        binding.googleSignIn.isEnabled = false
        binding.facebookLogin.isEnabled = false
        binding.loginButton.isEnabled = false
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

                if (account != null){
                    val authorizationCode = account.serverAuthCode.toString()
                    val userTokenId = account.idToken.toString()
                    binding.progressBar.visibility = View.INVISIBLE
                    updateUI(authorizationCode,userTokenId)
                }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            binding.googleSignIn.isEnabled = true
            binding.facebookLogin.isEnabled = true
            binding.loginButton.isEnabled = true
            binding.progressBar.visibility = View.INVISIBLE
            binding.root.snackBar("Login with google failed!!")
        }
    }

     private fun updateUI(authorizationCode :String, userTokenId :String ){
        val carApi = ListingCarsAPI(networkConnectionInterceptor)
         binding.progressBar.visibility = View.VISIBLE
        val call =  carApi.getAccessTokenGoogle(
            url = getString(R.string.google_get_access_token_url),
            grant_type = "authorization_code",
            client_id = getString(R.string.web_client_id),
            client_secret = getString(R.string.web_client_secret),
            redirect_uri = "",
            authCode = authorizationCode,
            id_token = userTokenId)
         call.enqueue(object : Callback<GoogleSignInAccessTokenDataClass>{
             val tag = "getGoogleAccessToken"
             override fun onFailure(call: Call<GoogleSignInAccessTokenDataClass>, t: Throwable) {
                 Log.e(tag, t.toString())
             }

             override fun onResponse(
                 call: Call<GoogleSignInAccessTokenDataClass>,
                 response: Response<GoogleSignInAccessTokenDataClass>
             ) {

                 if (response.isSuccessful){
                     val responseBody = response.body()
                     val accessToken = responseBody!!.access_token
                     val customAlertDialog = CustomAlertDialog(requireActivity())
                     customAlertDialog.startLoadingDialog("Updating User Profile")
                     viewModel.getSignedInWithGoogleAccount(accessToken)
                     try {
                         viewModel.loggedInUseWithGoogleAccountResponse.observe(viewLifecycleOwner, Observer {
                             binding.progressBar.visibility = View.INVISIBLE
                             customAlertDialog.stopDialog()
                             if (it.userObject?.id != null){
                                 binding.root.snackBar(it.message)
                                 session.saveSession(it.userObject.id)
                                 session.saveUserName(it.userObject.google?.name!!)
                                 session.saveUserEmail(it.userObject.google.email!!)
                                 session.saveUserPhoneNumber(it.userObject.phoneNumber?.number!!)
                                 session.saveUserPhoneNumberVerified(it.userObject.phoneNumber.verified!!)
                                 session.saveUserSignInMethod(it.userObject.method!!)
                                 findNavController().popBackStack(R.id.indexFragment, true)
                                 findNavController().popBackStack(R.id.signInUserFragment, true)
                                 if (it.userObject.phoneNumber.verified == false){
                                     loadVerifyPhoneFragment()
                                 }
                                 else{
                                     loadHomeActivity()
                                 }
                             }

                         })
                     }catch (e :NoInternetException){
                         binding.root.snackBar(e.message)
                     }
                 }else{
                     try {
                         val responseError = response.errorBody()!!.string()
                         Log.e(tag, responseError)
                     }catch (e:Exception){Log.e(tag, e.toString())}
                 }
             }
         })
     }


    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        
    }

    private fun loadHomeActivity() {
        findNavController().popBackStack(R.id.signInUserFragment, true)
        findNavController().navigate(R.id.indexFragment)
    }

    private fun loadVerifyPhoneFragment() {
        findNavController().navigate(R.id.phoneNumberFragment)
    }


    companion object {
        private const val RC_SIGN_IN = 100
    }

}