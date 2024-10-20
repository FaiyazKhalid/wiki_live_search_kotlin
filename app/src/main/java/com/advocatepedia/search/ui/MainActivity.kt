package com.advocatepedia.search.ui


import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.advocatepedia.search.R
import com.advocatepedia.search.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var viewState: MainViewState

    }



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        lateinit var recyclerView: RecyclerView
        lateinit var myAdapter: MyAdapter

        supportActionBar?.setDisplayShowTitleEnabled(false);
        //supportActionBar?.setIcon(R.drawable.app_logo);
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = Builder(navController.graph).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        viewState = MainViewState()
        binding.viewState = viewState



        lateinit var stateSpinner: Spinner
        lateinit var courtSpinner: Spinner


        val courtsMap: MutableMap<String, List<String>> = mutableMapOf()

        stateSpinner = findViewById(com.advocatepedia.search.R.id.state_spinner)
        courtSpinner = findViewById(com.advocatepedia.search.R.id.court_spinner)


        val states = listOf(
            "Supreme Court of India",
            "All High Courts",
            "All Tribunals",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
            "Andaman and Nicobar Islands",
            "Chandigarh",
            "Dadra and Nagar Haveli and Daman and Diu",
            "Lakshadweep",
            "Puducherry",
            "Ladakh",
            "Jammu and Kashmir"
        )
        courtsMap["Supreme Court of India"] = listOf("Select Court","Supreme Court of India")
        courtsMap["All High Courts"] = listOf("Select Court",
            "Andhra Pradesh High Court",
            "Bombay High Court",
            "Calcutta High Court",
            "Chhattisgarh High Court",
            "Delhi High Court",
            "Gauhati High Court",
            "Gujarat High Court",
            "Himachal Pradesh High Court",
            "Jammu & Kashmir and Ladakh High Court",
            "Jharkhand High Court",
            "Karnataka High Court",
            "Kerala High Court",
            "Madhya Pradesh High Court",
            "Madras High Court",
            "Manipur High Court",
            "Meghalaya High Court",
            "Orissa High Court",
            "Patna High Court",
            "Punjab and Haryana High Court",
            "Rajasthan High Court",
            "Sikkim High Court",
            "Telangana High Court",
            "Tripura High Court",
            "Uttarakhand High Court",
            "Allahabad High Court"
        )
        courtsMap["All Tribunals"] = listOf("Select Court",
            "Armed Forces Tribunal (AFT)",
            "Central Administrative Tribunal (CAT)",
            "Income Tax Appellate Tribunal (ITAT)",
            "Customs, Excise and Service Tax Appellate Tribunal (CESTAT)",
            "National Green Tribunal (NGT)",
            "Competition Appellate Tribunal (COMPAT)",
            "Securities Appellate Tribunal (SAT)",
            "Telecom Disputes Settlement and Appellate Tribunal (TDSAT)",
            "Appellate Tribunal for Electricity (ATE)",
            "Authority for Advance Rulings (AAR)",
            "Cyber Appellate Tribunal",
            "Employees Provident Fund Appellate Tribunal",
            "Intellectual Property Appellate Board (IPAB)",
            "Company Law Board (CLB)",
            "Competition Commission of India (CCI)",
            "Insurance Regulatory and Development Authority (IRDA)",
            "Securities and Exchange Board of India (SEBI)",
            "Telecom Regulatory Authority of India (TRAI)"
        )

        courtsMap["Andhra Pradesh"] = listOf("Select Court",
            "Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna",
            "Kurnool", "Prakasam", "Srikakulam", "Visakhapatnam", "Vizianagaram",
            "West Godavari", "YSR Kadapa", "Nellore"
        )
        courtsMap["Arunachal Pradesh"] = listOf("Select Court",
            "Anjaw", "Changlang", "Dibang Valley", "East Kameng", "East Siang",
            "Kurung Kumey", "Lower Dibang Valley", "Lower Subansiri", "Papum Pare",
            "Tawang", "Tirap", "Upper Siang", "Upper Subansiri", "West Kameng",
            "West Siang", "Lohit", "Kra Daadi", "Namsai", "Siang", "Longding"
        )
        courtsMap["Assam"] = listOf("Select Court",
            "Bongaigaon", "Darrang", "Dibrugarh", "Karimganj", "Udalguri",
            "Tinsukia", "Baksa", "Barpeta", "Cachar", "Chirang", "Dhemaji",
            "Dhubri", "Goalpara", "Golaghat", "Hailakandi", "Jorhat", "Kamrup",
            "Kamrup Metropolitan", "Karbi Anglong", "Kokrajhar", "Lakhimpur",
            "Morigaon", "Nagaon", "Nalbari", "Dima Hasao", "Sivasagar",
            "Sonitpur", "Charaideo", "Hojai", "West Karbi Anglong", "South Salmara-Mankachar",
            "Bajali", "Biswanath", "Majuli"
        )
        courtsMap["Bihar"] = listOf("Select Court",
            "Araria", "Aurangabad", "Banka", "Begusarai", "Bhagalpur",
            "Bhojpur", "Buxar", "Darbhanga", "East Champaran", "Gaya",
            "Gopalganj", "Jamui", "Jehanabad", "Kaimur (Bhabua)", "Katihar",
            "Khagaria", "Kishanganj", "Lakhisarai", "Madhepura", "Madhubani",
            "Munger", "Muzaffarpur", "Nalanda", "Nawada", "Patna", "Purnea",
            "Rohtas", "Saharsa", "Samastipur", "Saran", "Sheikhpura",
            "Sheohar", "Sitamarhi", "Siwan", "Supaul", "Vaishali", "West Champaran"
        )
        courtsMap["Chhattisgarh"] = listOf("Select Court",
            "Bilaspur", "Bastar", "Bijapur", "Dhamtari", "Durg",
            "Janjgir-Champa", "Jashpur", "Kabirdham (Kawardha)", "Korba", "Korea",
            "Mahasamund", "North Bastar (Kanker)", "Raigarh", "Raipur", "Rajnandgaon",
            "South Bastar (Dantewada)", "Surguja", "Surajpur", "Bemetara", "Balod",
            "Balodabazar", "Kondagaon", "Garibandh", "Mungeli", "Balrampur",
            "Chhattisgarh", "Dadra and Nagar Haveli", "Chandigarh"
        )
        courtsMap["Goa"] =
            listOf("Select Court","North Goa District Court", "South Goa District Court", "Quepem District Court")
        courtsMap["Gujarat"] = listOf("Select Court",
            "Ahmedabad", "Amreli", "Anand", "Banaskantha", "Bharuch",
            "Bhavnagar", "Dahod", "Dang", "Gandhinagar", "Jamnagar",
            "Junagadh", "Kheda", "Kutch", "Mahesana", "Narmada",
            "Navsari", "Panchmahals", "Patan", "Porbandar", "Rajkot",
            "Sabarkantha", "Surat", "Surendranagar", "Tapi", "Vadodara",
            "Valsad", "Ahmedabad (Rural)", "Gir Somnath", "Aravalli", "Morbi",
            "Devbhumi Dwarka", "Chhota Udepur", "Mahisagar"
        )
        courtsMap["Haryana"] = listOf("Select Court",
            "Ambala", "Bhiwani", "Charkhi Dadri", "Faridabad", "Fatehabad",
            "Gurgaon", "Hisar", "Jhajjar", "Jind", "Kaithal",
            "Karnal", "Kurukshetra", "Mahendragarh", "Mewat", "Palwal",
            "Panchkula", "Panipat", "Rewari", "Rohtak", "Sirsa",
            "Sonipat", "Yamunanagar"
        )
        courtsMap["Himachal Pradesh"] = listOf("Select Court",
            "Bilaspur", "Chamba", "Hamirpur", "Kangra", "Kinnaur",
            "Lahaul and Spiti", "Mandi", "Shimla", "Sirmaur", "Solan", "Una"
        )
        courtsMap["Jharkhand"] = listOf("Select Court",
            "Bokaro", "Chatra", "Deoghar", "Dhanbad", "Dumka",
            "East Singhbhum", "Garhwa", "Giridih", "Godda", "Gumla",
            "Hazaribagh", "Jamtara", "Khunti", "Koderma", "Latehar",
            "Lohardaga", "Pakur", "Palamu", "Ramgarh", "Ranchi",
            "Sahebganj", "Seraikela Kharsawan", "Simdega", "West Singhbhum"
        )
        courtsMap["Karnataka"] = listOf("Select Court",
            "Bagalkot", "Ballari", "Belagavi", "Bengaluru Rural", "Bengaluru Urban",
            "Bidar", "Chamarajanagar", "Chikkaballapur", "Chikkamagaluru", "Chitradurga",
            "Dakshina Kannada", "Davanagere", "Dharwad", "Gadag", "Hassan",
            "Haveri", "Kalaburagi", "Kodagu", "Kolar", "Koppal",
            "Mandya", "Mysuru", "Raichur", "Ramanagara", "Shivamogga",
            "Tumakuru", "Udupi", "Uttara Kannada", "Vijayapura", "Yadgir"
        )
        courtsMap["Kerala"] = listOf("Select Court",
            "Alappuzha",
            "Ernakulam",
            "Idukki",
            "Kannur",
            "Kasargod",
            "Kollam",
            "Kottayam",
            "Malappuram",
            "Palakkad",
            "Pathanamthitta",
            "Thiruvananthapuram",
            "Thrissur",
            "Wayanad",
            "Kozhikode"
        )
        courtsMap["Madhya Pradesh"] = listOf("Select Court",
            "District & Session Court AGAR",
            "Civil Court NALKHEDA",
            "Civil Court SUSNER",
            "District & Session Court ALIRAJPUR",
            "Civil Court JOBAT",
            "District & Session Court ANUPPUR",
            "Civil Court KOTMA",
            "Civil Court RAJENDRA GRAM",
            "District & Session Court ASHOKNAGAR",
            "Civil Court CHANDERI",
            "Civil Court MUGAWALI",
            "District & Session Court BALAGHAT",
            "Civil Court KATANGI",
            "Civil Court WARA SEONI",
            "Civil Court BAIHAR",
            "Civil Court LANJI",
            "District & Session Court BARWANI",
            "Civil Court RAJ PUR",
            "Civil Court ANJAD",
            "Civil Court SENDHWA",
            "Civil Court Khetia",
            "District & Session Court BETUL",
            "Civil Court AMLA",
            "Civil Court BHAINSDEHI",
            "Civil Court MULTAI",
            "District & Session Court BHIND",
            "Civil Court MEHGAON",
            "Civil Court LAHAR",
            "Civil Court GOHAD",
            "District & Session Court BHOPAL",
            "Civil Court BERASIA",
            "District & Session Court BURHANPUR",
            "District & Session Court CHHATARPUR",
            "Civil Court BADAMALAHARA",
            "Civil Court BIJAWAR",
            "Civil Court LAUNDI",
            "Civil Court NOWGONG",
            "Civil Court RAJNAGAR",
            "District & Session Court CHHINDWARA",
            "Civil Court HARRAI",
            "Civil Court TAMIA",
            "Civil Court PARASIA",
            "Civil Court SAUSAR",
            "Civil Court PANDHURNA",
            "Civil Court AMARWADA",
            "Civil Court CHOURAI",
            "Civil Court Junardeo (Jamai)",
            "District & Session Court DAMOH",
            "Civil Court PATHARIA",
            "Civil Court HATTA",
            "Civil Court Tendukheda",
            "District & Session Court DATIA",
            "Civil Court SEONDHA",
            "Civil Court BHANDER",
            "District & Session Court DEWAS",
            "Civil Court TONKKHURD",
            "Civil Court SONKATCH",
            "Civil Court BAGLI",
            "Civil Court KANNOD",
            "Civil Court KHATEGAON",
            "District & Session Court DHAR",
            "Civil Court BADNAVAR",
            "Civil Court DHARAMPURI",
            "Civil Court KUKSHI",
            "Civil Court MANAVAR",
            "Civil Court SARDARPUR",
            "District & Session Court DINDORI",
            "District & Session Court SHAHPURA",
            "District & Session Court GUNA",
            "Civil Court ARON",
            "Civil Court Kumbhraj",
            "Civil Court CHACHODA",
            "Civil Court RAGHOGARH",
            "District & Session Court GWALIOR",
            "Civil Court DABRA",
            "Civil Court BHITARWAR",
            "District & Session Court HARDA",
            "Civil Court TIMARNI",
            "District & Session Court HOSHANGABAD",
            "Civil Court Pachmadi",
            "Civil Court ITARSI",
            "Civil Court SOHAGPUR",
            "Civil Court PIPERIYA",
            "Civil Court SIVNI MAALVA",
            "District & Session Court INDORE",
            "Civil Court MHOW",
            "Civil Court DEPALPUR",
            "Civil Court SANWER",
            "Civil Court Hatod",
            "District & Session Court JABALPUR",
            "Civil Court PATAN",
            "Civil Court SIHORA",
            "District & Session Court JHABUA",
            "Civil Court THANDLA",
            "Civil Court PETLAWAD",
            "District & Session Court KATNI",
            "Civil Court VIJAY RAGHAVGARH",
            "Civil Court BARHI",
            "Civil Court Dheemarkhera",
            "District & Session Court KHANDWA",
            "Civil Court HARSUD",
            "Civil Court Punasa",
            "District & Session Court MANDLESHWAR",
            "Civil Court SANAWAD",
            "Civil Court BHIKAN GOAN",
            "Civil Court KASRAVAD",
            "Civil Court BARWAHA",
            "Civil Court MAHESHWAR",
            "Civil Court KHARGONE",
            "District & Session Court MANDLA",
            "Civil Court BICHHIA",
            "Civil Court NARAYANGANJ",
            "Civil Court NAINPUR",
            "Civil Court NIWAS",
            "District & Session Court MANDSAUR",
            "Civil Court BHANPURA",
            "Civil Court GAROTH",
            "Civil Court Narayangarh",
            "Civil Court SITAMAU",
            "District & Session Court MORENA",
            "Civil Court AMBAH",
            "Civil Court JOURA",
            "Civil Court SABALGARH",
            "District & Session Court NARSINGHPUR",
            "Civil Court GADARWADA",
            "Civil Court Tendukheda",
            "District & Session Court NEEMUCH",
            "Civil Court JAWAD",
            "Civil Court MANASA",
            "District & Session Court Panna",
            "Civil Court AJAIGARH",
            "Civil Court Pawai",
            "District & Session Court RAISEN",
            "Civil Court BARELI",
            "Civil Court BEGAMGANJ",
            "Civil Court GAIRATGANJ",
            "Civil Court GOHARGANJ",
            "Civil Court SILWANI",
            "Civil Court UDAIPURA",
            "District & Session Court RAJGARH",
            "Civil Court BIAORA",
            "Civil Court NARSINGHGARH",
            "Civil Court KHILCHIPUR",
            "Civil Court Jeerapur",
            "Civil Court Sarangpur",
            "District & Session Court RATLAM",
            "Civil Court ALOT",
            "Civil Court JAORA",
            "Civil Court SAILANA",
            "District & Session Court REWA",
            "Civil Court MAUGANJ",
            "Civil Court SIRMOUR",
            "Civil Court TEONTHAR",
            "Civil Court HANUMANA",
            "District & Session Court SAGAR",
            "Civil Court GARHAKOTA",
            "Civil Court BEENA",
            "Civil Court KHURAI",
            "Civil Court DEORI",
            "Civil Court BANDA",
            "Civil Court REHLI",
            "District & Session Court SATNA",
            "Civil Court AMARPATAN",
            "Civil Court MAIHAR",
            "Civil Court NAGOD",
            "Civil Court RAMPUR BAGHELAN",
            "Civil Court UNCHERA",
            "Civil Court CHITRAKOOT",
            "District & Session Court SEHORE",
            "Civil Court ASHTA",
            "Civil Court BUDHANI",
            "Civil Court ICHHAWAR",
            "Civil Court NASRULLAGANJ",
            "District & Session Court SEONI",
            "Civil Court GHANSORE",
            "Civil Court LAKHNADON",
            "District & Session Court SHAHDOL",
            "Civil Court BEOHARI",
            "Civil Court BURHAR",
            "Civil Court JAISINGHNAGAR",
            "District & Session Court SHAJAPUR",
            "Civil Court Sarangpur",
            "Civil Court Shujalpur",
            "District & Session Court SHEOPUR",
            "Civil Court VIJAYPUR",
            "District & Session Court SHIVPURI",
            "Civil Court KOLARAS",
            "Civil Court KHANIYADHANA",
            "Civil Court POHRI",
            "Civil Court KARAIRA",
            "Civil Court PICHHORE",
            "District & Session Court SIDHI",
            "Civil Court CHURHAT",
            "Civil Court MAJHAULI",
            "Civil Court RAMPUR NAIKIN",
            "District & Session Court SINGROULI",
            "Civil Court DEVSAR",
            "District & Session Court TIKAMGARH",
            "Civil Court JATARA",
            "Civil Court NIWARI",
            "Civil Court ORCHHA",
            "District & Session Court UJJAIN",
            "Civil Court NAGDA",
            "Civil Court BARNAGAR",
            "Civil Court KHACHROD",
            "Civil Court TARANA",
            "Civil Court MAHIDPUR",
            "District & Session Court UMARIA",
            "Civil Court MANPUR",
            "Civil Court Birsinghpur PALI",
            "District & Session Court VIDISHA",
            "Civil Court BASODA",
            "Civil Court KURWAI",
            "Civil Court LATERI",
            "Civil Court SIRONJ"
        )
        courtsMap["Maharashtra"] = listOf("Select Court",
            "Akola", "Aurangabad", "Chandrapur", "Jalgaon", "Kolhapur",
            "Nagpur", "Nanded", "Nashik", "Pune", "Solapur", "Thane",
            "Ahmednagar", "Amravati", "Beed", "Bhandara", "Buldhana",
            "Dhule", "Gadchiroli", "Gondia", "Jalna", "Latur", "Nandurbar",
            "Osmanabad", "Parbhani", "Raigad", "Ratnagiri", "Sangli",
            "Satara", "Sindhudurg", "Wardha", "Washim", "Yavatmal"
        )

        courtsMap["Manipur"] = listOf("Select Court",
            "Bishnupur", "Churachandpur", "Imphal East", "Imphal West",
            "Senapati", "Tamenglong", "Thoubal", "Ukhrul"
        )

        courtsMap["Meghalaya"] = listOf("Select Court",
            "East Garo Hills",
            "East Jaintia Hills",
            "East Khasi Hills",
            "North Garo Hills",
            "Ri Bhoi",
            "South Garo Hills",
            "South West Garo Hills",
            "South West Khasi Hills",
            "West Garo Hills",
            "West Jaintia Hills",
            "West Khasi Hills"
        )

        courtsMap["Mizoram"] = listOf("Select Court",
            "Aizawl",
            "Champhai",
            "Kolasib",
            "Lawngtlai",
            "Lunglei",
            "Mamit",
            "Saiha",
            "Serchhip"
        )

        courtsMap["Nagaland"] = listOf("Select Court",
            "Dimapur",
            "Kiphire",
            "Kohima",
            "Longleng",
            "Mokokchung",
            "Mon",
            "Peren",
            "Phek",
            "Tuensang",
            "Wokha",
            "Zunheboto"
        )

        courtsMap["Odisha"] = listOf("Select Court",
            "Angul",
            "Balangir",
            "Balasore",
            "Bargarh",
            "Bhadrak",
            "Boudh",
            "Cuttack",
            "Deogarh",
            "Dhenkanal",
            "Gajapati",
            "Ganjam",
            "Jagatsinghapur",
            "Jajpur",
            "Jharsuguda",
            "Kalahandi",
            "Kandhamal",
            "Kendrapara",
            "Kendujhar",
            "Khordha",
            "Koraput",
            "Malkangiri",
            "Mayurbhanj",
            "Nabarangpur",
            "Nayagarh",
            "Nuapada",
            "Puri",
            "Rayagada",
            "Sambalpur",
            "Subarnapur",
            "Sundargarh"
        )

        courtsMap["Punjab"] = listOf("Select Court",
            "Amritsar",
            "Barnala",
            "Bathinda",
            "Faridkot",
            "Fatehgarh Sahib",
            "Fazilka",
            "Ferozepur",
            "Gurdaspur",
            "Hoshiarpur",
            "Jalandhar",
            "Kapurthala",
            "Ludhiana",
            "Mansa",
            "Moga",
            "Muktsar",
            "Nawanshahr",
            "Pathankot",
            "Patiala",
            "Rupnagar",
            "Sangrur",
            "SAS Nagar",
            "Tarn Taran"
        )

        courtsMap["Rajasthan"] = listOf("Select Court",
            "Ajmer",
            "Alwar",
            "Banswara",
            "Baran",
            "Barmer",
            "Bharatpur",
            "Bhilwara",
            "Bikaner",
            "Bundi",
            "Chittorgarh",
            "Churu",
            "Dausa",
            "Dholpur",
            "Dungarpur",
            "Hanumangarh",
            "Jaipur",
            "Jaisalmer",
            "Jalore",
            "Jhalawar",
            "Jhunjhunu",
            "Jodhpur",
            "Karauli",
            "Kota",
            "Nagaur",
            "Pali",
            "Pratapgarh",
            "Rajsamand",
            "Sawai Madhopur",
            "Sikar",
            "Sirohi",
            "Sri Ganganagar",
            "Tonk",
            "Udaipur"
        )

        courtsMap["Sikkim"] = listOf("Select Court","East Sikkim", "North Sikkim", "South Sikkim", "West Sikkim")

        courtsMap["Tamil Nadu"] = listOf("Select Court",
            "Ariyalur",
            "Chengalpattu",
            "Chennai",
            "Coimbatore",
            "Cuddalore",
            "Dharmapuri",
            "Dindigul",
            "Erode",
            "Kallakurichi",
            "Kanchipuram",
            "Kanyakumari",
            "Karur",
            "Krishnagiri",
            "Madurai",
            "Nagapattinam",
            "Namakkal",
            "Nilgiris",
            "Perambalur",
            "Pudukkottai",
            "Ramanathapuram",
            "Ranipet",
            "Salem",
            "Sivaganga",
            "Tenkasi",
            "Thanjavur",
            "Theni",
            "Thoothukudi",
            "Tiruchirappalli",
            "Tirunelveli",
            "Tirupathur",
            "Tiruppur",
            "Tiruvallur",
            "Tiruvannamalai",
            "Tiruvarur",
            "Vellore",
            "Viluppuram",
            "Virudhunagar"
        )

        courtsMap["Telangana"] = listOf("Select Court",
            "Adilabad",
            "Bhadradri Kothagudem",
            "Hyderabad",
            "Jagtial",
            "Jangaon",
            "Jayashankar Bhupalapally",
            "Jogulamba Gadwal",
            "Kamareddy",
            "Karimnagar",
            "Khammam",
            "Kumuram Bheem Asifabad",
            "Mahabubabad",
            "Mahbubnagar",
            "Mancherial",
            "Medak",
            "Medchal-Malkajgiri",
            "Mulugu",
            "Nagarkurnool",
            "Nalgonda",
            "Narayanpet",
            "Nirmal",
            "Nizamabad",
            "Peddapalli",
            "Rajanna Sircilla",
            "Rangareddy",
            "Sangareddy",
            "Siddipet",
            "Suryapet",
            "Vikarabad",
            "Wanaparthy",
            "Warangal",
            "Yadadri Bhuvanagiri"
        )

        courtsMap["Tripura"] = listOf("Select Court",
            "Dhalai",
            "Gomati",
            "Khowai",
            "North Tripura",
            "Sepahijala",
            "South Tripura",
            "Unakoti",
            "West Tripura"
        )

        courtsMap["Uttar Pradesh"] = listOf("Select Court",
            "Agra",
            "Aligarh",
            "Allahabad",
            "Ambedkar Nagar",
            "Amethi",
            "Amroha",
            "Auraiya",
            "Azamgarh",
            "Baghpat",
            "Bahraich",
            "Ballia",
            "Balrampur",
            "Banda",
            "Barabanki",
            "Bareilly",
            "Basti",
            "Bhadohi",
            "Bijnor",
            "Budaun",
            "Bulandshahr",
            "Chandauli",
            "Chitrakoot",
            "Deoria",
            "Etah",
            "Etawah",
            "Farrukhabad",
            "Fatehpur",
            "Firozabad",
            "Gautam Buddha Nagar",
            "Ghaziabad",
            "Ghazipur",
            "Gonda",
            "Gorakhpur",
            "Hamirpur",
            "Hapur",
            "Hardoi",
            "Hathras",
            "Jalaun",
            "Jaunpur",
            "Jhansi",
            "Kannauj",
            "Kanpur Dehat",
            "Kanpur Nagar",
            "Kasganj",
            "Kaushambi",
            "Kheri",
            "Kushinagar",
            "Lalitpur",
            "Lucknow",
            "Maharajganj",
            "Mahoba",
            "Mainpuri",
            "Mathura",
            "Mau",
            "Meerut",
            "Mirzapur",
            "Moradabad",
            "Muzaffarnagar",
            "Pilibhit",
            "Pratapgarh",
            "Rae Bareli",
            "Rampur",
            "Saharanpur",
            "Sambhal",
            "Sant Kabir Nagar",
            "Shahjahanpur",
            "Shamli",
            "Shravasti",
            "Siddharthnagar",
            "Sitapur",
            "Sonbhadra",
            "Sultanpur",
            "Unnao",
            "Varanasi"
        )

        courtsMap["Uttarakhand"] = listOf("Select Court",
            "Almora",
            "Bageshwar",
            "Chamoli",
            "Champawat",
            "Dehradun",
            "Haridwar",
            "Nainital",
            "Pauri Garhwal",
            "Pithoragarh",
            "Rudraprayag",
            "Tehri Garhwal",
            "Udham Singh Nagar",
            "Uttarkashi"
        )

        courtsMap["West Bengal"] = listOf("Select Court",
            "Alipurduar",
            "Bankura",
            "Birbhum",
            "Cooch Behar",
            "Dakshin Dinajpur",
            "Darjeeling",
            "Hooghly",
            "Howrah",
            "Jalpaiguri",
            "Jhargram",
            "Kalimpong",
            "Kolkata",
            "Malda",
            "Murshidabad",
            "Nadia",
            "North 24 Parganas",
            "Paschim Bardhaman",
            "Paschim Medinipur",
            "Purba Bardhaman",
            "Purba Medinipur",
            "Purulia",
            "South 24 Parganas",
            "Uttar Dinajpur"
        )

        courtsMap["Andaman and Nicobar Islands"] =
            listOf("Select Court","Nicobar", "North and Middle Andaman", "South Andaman")

        courtsMap["Chandigarh"] = listOf("Select Court","Chandigarh")
        courtsMap["Dadra and Nagar Haveli and Daman and Diu"] =
            listOf("Select Court","Dadra and Nagar Haveli", "Daman", "Diu")
        courtsMap["Lakshadweep"] = listOf("Select Court","Lakshadweep")
        courtsMap["Delhi"] = listOf(
            "Select Court",
            "Patiala House Court Complex",
            "Rouse Avenue Court Complex",
            "Karkardooma Court Complex",
            "Saket District Court",
            "Tis Hazari Court",
            "Rohini Court",
            "Dwarka Court"
        )

        courtsMap["Puducherry"] = listOf("Select Court","Karaikal", "Mahe", "Puducherry", "Yanam")

        courtsMap["Ladakh"] = listOf("Select Court","Kargil", "Leh")
        courtsMap["Jammu and Kashmir"] = listOf("Select Court",
            "Anantnag",
            "Bandipora",
            "Baramulla",
            "Budgam",
            "Doda",
            "Ganderbal",
            "Jammu",
            "Kathua",
            "Kishtwar",
            "Kulgam",
            "Kupwara",
            "Poonch",
            "Pulwama",
            "Rajouri",
            "Ramban",
            "Reasi",
            "Samba",
            "Shopian",
            "Srinagar",
            "Udhampur"
        )


        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner.adapter = stateAdapter

        stateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedState = states[position]
                    val courts = courtsMap[selectedState] ?: emptyList()

                    val courtAdapter =
                        ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, courts)
                    courtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    courtSpinner.adapter = courtAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    courtSpinner.adapter = null
                }
            }

        courtSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedCourt = parent.getItemAtPosition(position) as String
                    val selectedState = stateSpinner.selectedItem as String

                    Snackbar.make(
                        view,
                        "Selected Court: $selectedCourt",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    if (position > 0) {
                        stateSpinner.isEnabled = false
                    }
                    if (position > 0) {
                        courtSpinner.isEnabled = false
                    }
                    val cardViewAdd = findViewById<CardView>(com.advocatepedia.search.R.id.addo)




                    val retrofitBuilderAdd = Retrofit.Builder()

                        .baseUrl("https://advocatepedia.com/feed/directory/$selectedState/$selectedCourt/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ApiInterface::class.java)

                    val timerAdd = Timer()
                    val refreshIntervalAdd = 10000L // 10 seconds

                    val refreshTaskAdd = object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                fetchData()
                            }
                        }

                        fun fetchData() {
                            val retrofitDataAdd = retrofitBuilderAdd.getProductData()
                            retrofitDataAdd.enqueue(object : Callback<MyData?> {

                                @SuppressLint("WrongViewCast")
                                override fun onResponse(
                                    call: Call<MyData?>,
                                    response: Response<MyData?>
                                ) {
                                    val responseBody = response.body()
                                    val productList = responseBody?.products

                                    if (productList != null) {
                                        val productContainer = LinearLayout(this@MainActivity)
                                        productContainer.orientation = LinearLayout.VERTICAL

                                        for (product in productList) {
                                            val productText = TextView(this@MainActivity).apply {
                                                // text = product.adddescription ?: "No description available" // Set default text if empty
                                                // textSize = 20f
                                                //  setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                                            }
                                            productContainer.addView(productText)

                                            val thumbnailImageView =
                                                findViewById<ImageView>(com.advocatepedia.search.R.id.addphoto)
                                            // Load the thumbnail image using your image library (e.g., Glide)
                                            Glide.with(this@MainActivity).load(product.addphoto)
                                                .into(thumbnailImageView)

                                            val addTitleTextView =
                                                findViewById<TextView>(com.advocatepedia.search.R.id.addtitle)
                                            addTitleTextView.text = product.addtitle

                                            val addDTextView =
                                                findViewById<TextView>(com.advocatepedia.search.R.id.adddescription)
                                            addDTextView.text = product.adddescription


                                        }

                                        cardViewAdd.addView(productContainer) // Add the container with all products to CardView

                                        // Schedule removal after 10 seconds
                                        val handler = Handler(Looper.getMainLooper())
                                        val runnable = Runnable {
                                            cardViewAdd.removeView(productContainer) // Remove the container after 10 seconds
                                        }
                                        handler.postDelayed(runnable, 9999)

                                        // Update share button behavior (assuming productTextNum holds phone number)
                                        findViewById<Button>(com.advocatepedia.search.R.id.shareButtonadd).setOnClickListener {
                                            val phoneNumber =
                                                productList[0].addenquiry // Get the phone number from the first product
                                            val message =
                                                findViewById<TextView>(com.advocatepedia.search.R.id.messageadd).text.toString()

                                            val whatsappUrl =
                                                "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                                                    Uri.encode(message)
                                                }"
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse(whatsappUrl)
                                            }
                                            startActivity(intent)
                                        }
                                    } else {
                                        // Handle no products found case
                                    }
                                }

                                override fun onFailure(call: Call<MyData?>, t: Throwable) {
                                    Log.d("Main Activity", "onFailure: " + t.message)
                                    // Handle network error (e.g., display an error message)
                                }
                            })
                        }
                    }

                    timerAdd.schedule(refreshTaskAdd, 0, refreshIntervalAdd)


// BIRTHDAY WISHES

                    val cardView1 = findViewById<CardView>(com.advocatepedia.search.R.id.wish)

                    val retrofitBuilder1 = Retrofit.Builder()
                        .baseUrl("https://advocatepedia.com/feed/directory/$selectedState/$selectedCourt/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ApiInterface::class.java)


                    val timer = Timer()
                    val refreshInterval = 10000L // 10 seconds

                    val refreshTask = object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                fetchData()
                            }
                        }


                        fun fetchData() {


                            val retrofitData1 = retrofitBuilder1.getProductData()
                            retrofitData1.enqueue(object : Callback<MyData?> {

                                @SuppressLint("WrongViewCast")
                                override fun onResponse(
                                    call: Call<MyData?>,
                                    response: Response<MyData?>
                                ) {
                                    val responseBody = response.body()
                                    val productList = responseBody?.products

                                    if (productList != null) {
                                        val productContainer =
                                            LinearLayout(this@MainActivity) // Create a container for product views
                                        productContainer.orientation =
                                            LinearLayout.VERTICAL // Set vertical orientation for stacking views

                                        for (product in productList) {
                                            val productText = TextView(this@MainActivity)
                                            productText.text = product.wish
                                            productText.textSize = 20f // Set desired text size
                                            productText.setTextColor(
                                                ContextCompat.getColor(
                                                    this@MainActivity,
                                                    R.color.black
                                                )
                                            ) // Set the desired text color

                                            productContainer.addView(productText)

                                            // Add phone number view if needed
                                            val productTextNum = TextView(this@MainActivity)
                                            //productTextNum.text = product.num
                                            productContainer.addView(productTextNum)
                                        }

                                        cardView1.addView(productContainer)


                                        val handler = Handler(Looper.getMainLooper())
                                        val runnable = Runnable {
                                            cardView1.removeView(productContainer)
                                        }
                                        handler.postDelayed(
                                            runnable,
                                            10000
                                        ) // Delay removal for 10 seconds (1000 milliseconds)

                                        // Update share button behavior (assuming productTextNum holds phone number)
                                        findViewById<Button>(com.advocatepedia.search.R.id.num).setOnClickListener {
                                            val phoneNumber =
                                                productList[0].num // Get the phone number from the first product
                                            val message =
                                                findViewById<TextView>(com.advocatepedia.search.R.id.messageText).text.toString()
                                            val msg = "Happy birthday! May your legal journey be filled with success and satisfaction."
                                            val whatsappUrl =
                                                "https://api.whatsapp.com/send?phone=$phoneNumber&text=$msg.%20%20%20%20%20%20%20%0a%0a${
                                                    Uri.encode(message)
                                                }"
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse(whatsappUrl)
                                            }
                                            startActivity(intent)
                                        }
                                    } else {
                                        // Handle no products found case
                                    }
                                }

                                override fun onFailure(call: Call<MyData?>, t: Throwable) {
                                    Log.d("Main Activity", "onFailure: " + t.message)
                                    // Handle network error (e.g., display an error message)
                                }
                            })
                        }
                    }
                    timer.schedule(refreshTask, 0, refreshInterval)

        // END BIRTHDAY WISHES


        // ON THIS DAY

        val cardView2 = findViewById<CardView>(R.id.otd)

        val retrofitBuilder2 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/directory/$selectedState/$selectedCourt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        val timer2 = Timer()
        val refreshInterval2 = 10000L // 10 seconds

        val refreshTask2 = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    fetchData()
                }
            }


            fun fetchData() {


                val retrofitData2 = retrofitBuilder2.getProductData()
                retrofitData2.enqueue(object : Callback<MyData?> {

                    @SuppressLint("WrongViewCast")
                    override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                        val responseBody = response.body()
                        val productList = responseBody?.products

                        if (productList != null) {
                            val productContainer =
                                LinearLayout(this@MainActivity) // Create a container for product views
                            productContainer.orientation =
                                LinearLayout.VERTICAL // Set vertical orientation for stacking views

                            for (product in productList) {
                                val productText = TextView(this@MainActivity)
                                productText.text = product.otd
                                productText.textSize = 20f // Set desired text size
                                productText.setTextColor(
                                    ContextCompat.getColor(
                                        this@MainActivity,
                                        R.color.black
                                    )
                                ) // Set the desired text color

                                productContainer.addView(productText)

                                // Add phone number view if needed
                                val productTextNum = TextView(this@MainActivity)
                                //productTextNum.text = product.num
                                productContainer.addView(productTextNum)
                            }

                            cardView2.addView(productContainer)


                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable {
                                cardView2.removeView(productContainer)
                            }
                            handler.postDelayed(
                                runnable,
                                9999
                            ) // Delay removal for 10 seconds (1000 milliseconds)
                            val productNames = productList.joinToString(separator = "\n") { it.otd }
                            val shareButton =
                                findViewById<Button>(com.advocatepedia.search.R.id.shareButton2) // Replace R.id.shareButton with your actual button ID
                            shareButton.setOnClickListener {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(Intent.EXTRA_TEXT, productNames)
                                startActivity(Intent.createChooser(shareIntent, "Share products"))
                            }
                        } else {
                            // Handle no products found case
                        }
                    }

                    override fun onFailure(call: Call<MyData?>, t: Throwable) {
                        Log.d("Main Activity", "onFailure: " + t.message)
                        // Handle network error (e.g., display an error message)
                    }
                })
            }
        }
        timer2.schedule(refreshTask2, 0, refreshInterval2)


        // END ON THIS DAY

        // FEATURED ADVOCATE

        val retrofitBuilder3 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/directory/$selectedState/$selectedCourt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData3 = retrofitBuilder3.getProductData()
        retrofitData3.enqueue(
            object : Callback<MyData?> {

                @SuppressLint("WrongViewCast")
                override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                    val responseBody = response.body()
                    val productList = responseBody?.products

                    if (productList != null && productList.isNotEmpty()) {
                        val product = productList[0] // Assuming you want the first product
                        val titleTextView = findViewById<TextView>(com.advocatepedia.search.R.id.title)
                        val descriptionTextView =
                            findViewById<TextView>(com.advocatepedia.search.R.id.description)
                        val thumbnailImageView =
                            findViewById<ImageView>(com.advocatepedia.search.R.id.photo)

                        titleTextView.text = product.title ?: "No title found"
                        descriptionTextView.text = product.description1 ?: "No description available"

                        // Load the thumbnail image using Picasso (replace with your Glide instance if needed)
                        Picasso.get().load(product.thumbnail).into(thumbnailImageView)


                    } else {
                        // Handle case where no products are found
                        // (e.g., display a message or hide the CardView)
                    }
                }

                override fun onFailure(call: Call<MyData?>, t: Throwable) {
                    Log.d("Main Activity", "onFailure: " + t.message)
                    // Handle network error (e.g., display an error message)
                }
            })


// END FEATURED ADVOCATE

        // NEWS JUDGMENT

        val cardView5 = findViewById<CardView>(R.id.news)

        val retrofitBuilder5 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/$selectedState/$selectedCourt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        val timer5 = Timer()
        val refreshInterval5 = 10000L // 10 seconds

        val refreshTask5 = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    fetchData()
                }
            }


            fun fetchData() {


                val retrofitData5 = retrofitBuilder5.getProductData()
                retrofitData5.enqueue(object : Callback<MyData?> {

                    @SuppressLint("WrongViewCast")
                    override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                        val responseBody = response.body()
                        val productList = responseBody?.products

                        if (productList != null) {
                            val productContainer =
                                LinearLayout(this@MainActivity) // Create a container for product views
                            productContainer.orientation =
                                LinearLayout.VERTICAL // Set vertical orientation for stacking views

                            for (product in productList) {
                                val productText = TextView(this@MainActivity)
                                productText.text = product.news
                                productText.textSize = 20f // Set desired text size
                                productText.setTextColor(
                                    ContextCompat.getColor(
                                        this@MainActivity,
                                        R.color.black
                                    )
                                ) // Set the desired text color

                                productContainer.addView(productText)

                                // Add phone number view if needed
                                val productTextNum = TextView(this@MainActivity)
                                //productTextNum.text = product.num
                                productContainer.addView(productTextNum)
                            }

                            cardView5.addView(productContainer)


                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable {
                                cardView5.removeView(productContainer)
                            }
                            handler.postDelayed(
                                runnable,
                                9999
                            ) // Delay removal for 10 seconds (1000 milliseconds)
                            val productNames =
                                productList.joinToString(separator = "\n") { it.news }
                            val shareButton =
                                findViewById<Button>(com.advocatepedia.search.R.id.shareButton4) // Replace R.id.shareButton with your actual button ID
                            shareButton.setOnClickListener {
                                val shareIntent = Intent(Intent.ACTION_SEND)
                                shareIntent.type = "text/plain"
                                shareIntent.putExtra(Intent.EXTRA_TEXT, productNames)
                                startActivity(Intent.createChooser(shareIntent, "Share products"))
                            }
                        } else {
                            // Handle no products found case
                        }
                    }

                    override fun onFailure(call: Call<MyData?>, t: Throwable) {
                        Log.d("Main Activity", "onFailure: " + t.message)
                        // Handle network error (e.g., display an error message)
                    }
                })
            }
        }
        timer5.schedule(refreshTask5, 0, refreshInterval5)


// END JUDGEMENT

        //  ARTICLES
        val cardView6 = findViewById<CardView>(R.id.articles)

        val retrofitBuilder6 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/directory/$selectedState/$selectedCourt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        val timer6 = Timer()
        val refreshInterval6 = 10000L // 10 seconds

        val refreshTask6 = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    fetchData()
                }
            }


            fun fetchData() {


                val retrofitData6 = retrofitBuilder6.getProductData()
                retrofitData6.enqueue(object : Callback<MyData?> {

                    @SuppressLint("WrongViewCast")
                    override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                        val responseBody = response.body()
                        val productList = responseBody?.products

                        if (productList != null) {
                            val productContainer =
                                LinearLayout(this@MainActivity) // Create a container for product views
                            productContainer.orientation =
                                LinearLayout.VERTICAL // Set vertical orientation for stacking views

                            for (product in productList) {
                                val productText = TextView(this@MainActivity)
                                productText.text = product.articles
                                productText.textSize = 20f // Set desired text size
                                productText.setTextColor(
                                    ContextCompat.getColor(
                                        this@MainActivity,
                                        R.color.black
                                    )
                                ) // Set the desired text color

                                productContainer.addView(productText)

                                // Add phone number view if needed
                                val productTextNum = TextView(this@MainActivity)
                                //productTextNum.text = product.num
                                productContainer.addView(productTextNum)
                            }

                            cardView6.addView(productContainer)


                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable {
                                cardView6.removeView(productContainer)
                            }
                            handler.postDelayed(
                                runnable,
                                9999
                            ) // Delay removal for 10 seconds (1000 milliseconds)

                        } else {
                            // Handle no products found case
                        }
                    }

                    override fun onFailure(call: Call<MyData?>, t: Throwable) {
                        Log.d("Main Activity", "onFailure: " + t.message)
                        // Handle network error (e.g., display an error message)
                    }
                })
            }
        }
        timer6.schedule(refreshTask6, 0, refreshInterval6)

    }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

}
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.advocatepedia.search.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == com.advocatepedia.search.R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, com.advocatepedia.search.R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}
