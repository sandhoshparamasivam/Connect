package com.orane.icliniqconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.kissmetrics.sdk.KISSmetricsAPI;

import com.orane.icliniqconnect.Model.Model;
import com.orane.icliniqconnect.network.JSONParser;
import com.orane.icliniqconnect.network.NetCheck;
import com.orane.icliniqconnect.retrofitServices.RetrofitService;
import com.orane.icliniqconnect.retrofitmodel.Content;
import com.orane.icliniqconnect.retrofitmodel.SlotTypeModel;
import com.orane.icliniqconnect.retrofitmodel.TimeslotModel;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class Doc_Consultation1 extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener
//       , DatePickerDialog.OnDateSetListener
{
    private TextView timeTextView;
    private TextView dateTextView;
    private CheckBox mode24Hours;
    private CheckBox modeDarkTime;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentTime;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateTime;
    private CheckBox vibrateDate;
    private CheckBox dismissTime;
    private CheckBox dismissDate;
    private CheckBox titleTime;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox enableSeconds;
    private CheckBox limitTimes;
    private CheckBox limitDates;
    private CheckBox highlightDates;

    private int i = 1;
    private final static String CLICK = "click";
    private final static String NEXT_DATA = "next";

    TextView tvtips1, tvtips2, tvtips3, tvtips4, tv_fam_agedets, tv_fam_weight, tv_fam_height, tv_fam_name;

    ImageView img_uparrow, profile_right_arrow, img_downarrow;
    Button btn_date, btn_submit;
    public String tit_val,rel_val,tit_id,mem_name,age_val,dob_val,gender_val,height_val,weight_val;

    Map<String, String> family_map = new HashMap<String, String>();
    Spinner spinner_lang, spinner_timerange, spinner_ccode, spinner_timezone,spinner_slot;
    Map<String, String> timerange_map = new HashMap<String, String>();
    public String famDets_text,  radio_id = "0",myself_id,relation_type_val,family_list,sel_country_code, sel_country_name, spintz, lang_name, Doc_id, cc_name, cons_select_date, fee_hp, fee_lp, cons_phone, cons_type_text, cons_type, ftrack_show, consult_id, cons_phno, Query, spec_val, lang_val, cccode, sel_timerange_code, sel_timerange, sel_timezone, content_str, timezone_str, times_values;
    JSONObject cons_booking_jsonobj, post_json, jsonobj, jsonobj1c;
    TextView tv_changetimezone, tv_tz_present, tv_fee_hp, tv_fee_lp;
    public static Activity fa;
    LinearLayout select_layout, parent_layout, vidoecons_layout, phonecons_layout;

    Map<String, String> cc_map = new HashMap<String, String>();
    EditText edt_phoneno;
    Map<String, String> lang_map = new HashMap<String, String>();
    String timerange_name;
    CountryCodePicker countryCodePicker;
    ImageView img_remove,img_edit_icon;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Login_Status = "Login_Status_key";
    LinearLayout famidets_layout, someone_layout, family_inner_layout;
    List<Content> timeSlotList =new ArrayList<>();
    int isSlotAvail;
    String id,book_type;
    String newDateStr,consulttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_consultation1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Model.kiss = KISSmetricsAPI.sharedAPI(Model.kissmetric_apikey, getApplicationContext());
        Model.upload_files = "";

        FlurryAgent.onPageView();

        img_edit_icon = findViewById(R.id.img_edit_icon);
        spinner_slot=findViewById(R.id.spinner_slot);
        spinner_slot.setVisibility(View.GONE);
        img_downarrow = findViewById(R.id.img_downarrow);
        profile_right_arrow = findViewById(R.id.profile_right_arrow);
        img_uparrow = findViewById(R.id.img_uparrow);

        tv_fam_name = findViewById(R.id.tv_fam_name);
        tv_fam_agedets = findViewById(R.id.tv_fam_agedets);
        tv_fam_height = findViewById(R.id.tv_fam_height);
        tv_fam_weight = findViewById(R.id.tv_fam_weight);

        famidets_layout = findViewById(R.id.famidets_layout);
        someone_layout = findViewById(R.id.someone_layout);
        family_inner_layout = findViewById(R.id.family_inner_layout);

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        spinner_lang = (Spinner) findViewById(R.id.spinner_lang);
        edt_phoneno = (EditText) findViewById(R.id.edt_phoneno);
        spinner_ccode = (Spinner) findViewById(R.id.spinner_ccode);

/*        modeDarkTime.setChecked(Utils.isDarkTheme(this, modeDarkTime.isChecked()));
        modeDarkDate.setChecked(Utils.isDarkTheme(this, modeDarkDate.isChecked()));*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color2));
        }

        //------------ Object Creations -------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Schedule the consultation");
        }
        //------------ Object Creations ------------------------------
        parent_layout = findViewById(R.id.parent_layout);

        tv_tz_present = (TextView) findViewById(R.id.tv_tz_present);
        tv_changetimezone = (TextView) findViewById(R.id.tv_changetimezone);
        spinner_timezone = (Spinner) findViewById(R.id.spinner_timezone);
        spinner_timerange = (Spinner) findViewById(R.id.spinner_timerange);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        //Typeface font_reg = Typeface.createFromAsset(getAssets(), Model.font_name);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), Model.font_name_bold);

        ((TextView) findViewById(R.id.tv_ph_tit)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_tz_present)).setTypeface(font_bold);
        ((TextView) findViewById(R.id.tv_schedule)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_date)).setTypeface(font_bold);
        ((Button) findViewById(R.id.btn_submit)).setTypeface(font_bold);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

//        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        newDateStr=simpleDate.format(c);
//        Log.e("date un display",newDateStr+" ");

//        String formattedDate = df.format(c);
//        Log.e("display date",formattedDate+" ");
        btn_date.setText(newDateStr);

        countryCodePicker.registerCarrierNumberEditText(edt_phoneno);

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                System.out.println("getSelectedCountryCode------------" + countryCodePicker.getSelectedCountryCode());
                System.out.println("getSelectedCountryCodeAsInt------------" + countryCodePicker.getSelectedCountryCodeAsInt());
                System.out.println("getSelectedCountryName------------" + countryCodePicker.getSelectedCountryName());
                System.out.println("getSelectedCountryNameCode------------" + countryCodePicker.getSelectedCountryNameCode());

                cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            }
        });



        img_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-------------------------------------------------------------------
                String get_family_profile = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&id=" + radio_id + "&isView=0&token=" + Model.token;
                System.out.println("get_family_profile---------" + get_family_profile);
                new JSON_EditFamDetails().execute(get_family_profile);
                //---------------------------------------------------------------

                //ask_someone("edit");
            }
        });




        countryCodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {
            cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
            System.out.println("Start cccode-----" + cccode);

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            //------ getting Values ---------------------------
            Intent intent = getIntent();
            Query = intent.getStringExtra("Query");
            cons_type = intent.getStringExtra("cons_type");
            Doc_id = intent.getStringExtra("Doc_id");
            //------ getting Values ---------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }

        //-------------------------------------------------------------------
        String get_family_url = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&token=" + Model.token;
        System.out.println("get_family_url---------" + get_family_url);
        new JSON_getFamily().execute(get_family_url);
        //-------------------------------------------------------------------



        //------- Setting Language ----------------------

//
        {
            final List<String> lang_categories = new ArrayList<String>();

            lang_categories.add("English");
            lang_map.put("English", "en");
            lang_categories.add("Hindi");
            lang_map.put("Hindi", "hi");
            lang_categories.add("Arabic");
            lang_map.put("Arabic", "ar");

            lang_categories.add("Abkhazian");
            lang_map.put("Abkhazian", "ab");
            lang_categories.add("Achinese");
            lang_map.put("Achinese", "ace");
            lang_categories.add("Acoli");
            lang_map.put("Acoli", "ach");
            lang_categories.add("Adangme");
            lang_map.put("Adangme", "ada");
            lang_categories.add("Adyghe");
            lang_map.put("Adyghe", "ady");
            lang_categories.add("Afar");
            lang_map.put("Afar", "aa");
            lang_categories.add("Afrihili");
            lang_map.put("Afrihili", "afh");
            lang_categories.add("Afrikaans");
            lang_map.put("Afrikaans", "af");
            lang_categories.add("Afro-Asiatic Language");
            lang_map.put("Afro-Asiatic Language", "afa");
            lang_categories.add("Ainu");
            lang_map.put("Ainu", "ain");
            lang_categories.add("Akan");
            lang_map.put("Akan", "ak");
            lang_categories.add("Akkadian");
            lang_map.put("Akkadian", "akk");
            lang_categories.add("Albanian");
            lang_map.put("Albanian", "sq");
            lang_categories.add("Aleut");
            lang_map.put("Aleut", "ale");
            lang_categories.add("Algonquian Language");
            lang_map.put("Algonquian Language", "alg");
            lang_categories.add("Altaic Language");
            lang_map.put("Altaic Language", "tut");
            lang_categories.add("Amharic");
            lang_map.put("Amharic", "am");
            lang_categories.add("Ancient Egyptian");
            lang_map.put("Ancient Egyptian", "egy");
            lang_categories.add("Ancient Greek");
            lang_map.put("Ancient Greek", "grc");
            lang_categories.add("Angika");
            lang_map.put("Angika", "anp");
            lang_categories.add("Apache Language");
            lang_map.put("Apache Language", "apa");

            lang_categories.add("Aragonese");
            lang_map.put("Aragonese", "an");
            lang_categories.add("Aramaic");
            lang_map.put("Aramaic", "arc");
            lang_categories.add("Arapaho");
            lang_map.put("Arapaho", "arp");
            lang_categories.add("Araucanian");
            lang_map.put("Araucanian", "arn");
            lang_categories.add("Arawak");
            lang_map.put("Arawak", "arw");
            lang_categories.add("Armenian");
            lang_map.put("Armenian", "hy");
            lang_categories.add("Aromanian");
            lang_map.put("Aromanian", "rup");
            lang_categories.add("Artificial Language");
            lang_map.put("Artificial Language", "art");
            lang_categories.add("Assamese");
            lang_map.put("Assamese", "as");
            lang_categories.add("Asturian");
            lang_map.put("Asturian", "ast");
            lang_categories.add("Athapascan Language");
            lang_map.put("Athapascan Language", "ath");
            lang_categories.add("Atsam");
            lang_map.put("Atsam", "cch");
            lang_categories.add("Australian English");
            lang_map.put("Australian English", "en_AU");
            lang_categories.add("Australian Language");
            lang_map.put("Australian Language", "aus");
            lang_categories.add("Austrian German");
            lang_map.put("Austrian German", "de_AT");
            lang_categories.add("Austronesian Language");
            lang_map.put("Austronesian Language", "map");
            lang_categories.add("Avaric");
            lang_map.put("Avaric", "av");
            lang_categories.add("Avestan");
            lang_map.put("Avestan", "ae");
            lang_categories.add("Awadhi");
            lang_map.put("Awadhi", "awa");
            lang_categories.add("Aymara");
            lang_map.put("Aymara", "ay");
            lang_categories.add("Azerbaijani");
            lang_map.put("Azerbaijani", "az");
            lang_categories.add("Balinese");
            lang_map.put("Balinese", "ban");
            lang_categories.add("Baltic Language");
            lang_map.put("Baltic Language", "bat");
            lang_categories.add("Baluchi");
            lang_map.put("Baluchi", "bal");
            lang_categories.add("Bambara");
            lang_map.put("Bambara", "bm");
            lang_categories.add("Bamileke Language");
            lang_map.put("Bamileke Language", "bai");
            lang_categories.add("Banda");
            lang_map.put("Banda", "bad");
            lang_categories.add("Bantu");
            lang_map.put("Bantu", "bnt");
            lang_categories.add("Basa");
            lang_map.put("Basa", "bas");
            lang_categories.add("Bashkir");
            lang_map.put("Bashkir", "ba");
            lang_categories.add("Basque");
            lang_map.put("Basque", "eu");
            lang_categories.add("Batak");
            lang_map.put("Batak", "btk");
            lang_categories.add("Beja");
            lang_map.put("Beja", "bej");
            lang_categories.add("Belarusian");
            lang_map.put("Belarusian", "be");
            lang_categories.add("Bemba");
            lang_map.put("Bemba", "bem");
            lang_categories.add("Bengali");
            lang_map.put("Bengali", "bn");
            lang_categories.add("Berber");
            lang_map.put("Berber", "ber");
            lang_categories.add("Bhojpuri");
            lang_map.put("Bhojpuri", "bho");
            lang_categories.add("Bihari");
            lang_map.put("Bihari", "bh");
            lang_categories.add("Bikol");
            lang_map.put("Bikol", "bik");
            lang_categories.add("Bini");
            lang_map.put("Bini", "bin");
            lang_categories.add("Bislama");
            lang_map.put("Bislama", "bi");
            lang_categories.add("Blin");
            lang_map.put("Blin", "byn");
            lang_categories.add("Blissymbols");
            lang_map.put("Blissymbols", "zbl");
            lang_categories.add("Bosnian");
            lang_map.put("Bosnian", "bs");
            lang_categories.add("Braj");
            lang_map.put("Braj", "bra");
            lang_categories.add("Brazilian Portuguese");
            lang_map.put("Brazilian Portuguese", "pt_BR");
            lang_categories.add("Breton");
            lang_map.put("Breton", "br");
            lang_categories.add("British English");
            lang_map.put("British English", "en_GB");
            lang_categories.add("Buginese");
            lang_map.put("Buginese", "bug");
            lang_categories.add("Bulgarian");
            lang_map.put("Bulgarian", "bg");
            lang_categories.add("Buriat");
            lang_map.put("Buriat", "bua");
            lang_categories.add("Burmese");
            lang_map.put("Burmese", "my");
            lang_categories.add("Caddo");
            lang_map.put("Caddo", "cad");
            lang_categories.add("Canadian English");
            lang_map.put("Canadian English", "en_CA");
            lang_categories.add("Canadian French");
            lang_map.put("Canadian French", "fr_CA");
            lang_categories.add("Carib");
            lang_map.put("Carib", "car");
            lang_categories.add("Catalan");
            lang_map.put("Catalan", "ca");
            lang_categories.add("Caucasian Language");
            lang_map.put("Caucasian Language", "cau");
            lang_categories.add("Cebuano");
            lang_map.put("Cebuano", "ceb");
            lang_categories.add("Celtic Language");
            lang_map.put("Celtic Language", "cel");
            lang_categories.add("Central American Indian Language");
            lang_map.put("Central American Indian Language", "cai");
            lang_categories.add("Chagatai");
            lang_map.put("Chagatai", "chg");
            lang_categories.add("Chamic Language");
            lang_map.put("Chamic Language", "cmc");
            lang_categories.add("Chamorro");
            lang_map.put("Chamorro", "ch");
            lang_categories.add("Chechen");
            lang_map.put("Chechen", "ce");
            lang_categories.add("Cherokee");
            lang_map.put("Cherokee", "chr");
            lang_categories.add("Cheyenne");
            lang_map.put("Cheyenne", "chy");
            lang_categories.add("Chibcha");
            lang_map.put("Chibcha", "chb");
            lang_categories.add("Chinese");
            lang_map.put("Chinese", "zh");
            lang_categories.add("Chinook Jargon");
            lang_map.put("Chinook Jargon", "chn");
            lang_categories.add("Chipewyan");
            lang_map.put("Chipewyan", "chp");
            lang_categories.add("Choctaw");
            lang_map.put("Choctaw", "cho");
            lang_categories.add("Church Slavic");
            lang_map.put("Church Slavic", "cu");
            lang_categories.add("Chuukese");
            lang_map.put("Chuukese", "chk");
            lang_categories.add("Chuvash");
            lang_map.put("Chuvash", "cv");
            lang_categories.add("Classical Newari");
            lang_map.put("Classical Newari", "nwc");
            lang_categories.add("Classical Syriac");
            lang_map.put("Classical Syriac", "syc");
            lang_categories.add("Coptic");
            lang_map.put("Coptic", "cop");
            lang_categories.add("Cornish");
            lang_map.put("Cornish", "kw");
            lang_categories.add("Corsican");
            lang_map.put("Corsican", "co");
            lang_categories.add("Cree");
            lang_map.put("Cree", "cr");
            lang_categories.add("Creek");
            lang_map.put("Creek", "mus");
            lang_categories.add("Creole or Pidgin");
            lang_map.put("Creole or Pidgin", "crp");
            lang_categories.add("Crimean Turkish");
            lang_map.put("Crimean Turkish", "crh");
            lang_categories.add("Croatian");
            lang_map.put("Croatian", "hr");
            lang_categories.add("Cushitic Language");
            lang_map.put("Cushitic Language", "cus");
            lang_categories.add("Czech");
            lang_map.put("Czech", "cs");
            lang_categories.add("Dakota");
            lang_map.put("Dakota", "dak");
            lang_categories.add("Danish");
            lang_map.put("Danish", "da");
            lang_categories.add("Dargwa");
            lang_map.put("Dargwa", "dar");
            lang_categories.add("Dayak");
            lang_map.put("Dayak", "day");
            lang_categories.add("Delaware");
            lang_map.put("Delaware", "del");
            lang_categories.add("Dinka");
            lang_map.put("Dinka", "din");
            lang_categories.add("Divehi");
            lang_map.put("Divehi", "dv");
            lang_categories.add("Dogri");
            lang_map.put("Dogri", "doi");
            lang_categories.add("Dogrib");
            lang_map.put("Dogrib", "dgr");
            lang_categories.add("Dravidian Language");
            lang_map.put("Dravidian Language", "dra");
            lang_categories.add("Duala");
            lang_map.put("Duala", "dua");
            lang_categories.add("Dutch");
            lang_map.put("Dutch", "nl");
            lang_categories.add("Dyula");
            lang_map.put("Dyula", "dyu");
            lang_categories.add("Dzongkha");
            lang_map.put("Dzongkha", "dz");
            lang_categories.add("Eastern Frisian");
            lang_map.put("Eastern Frisian", "frs");
            lang_categories.add("Efik");
            lang_map.put("Efik", "efi");
            lang_categories.add("Ekajuk");
            lang_map.put("Ekajuk", "eka");
            lang_categories.add("Elamite");
            lang_map.put("Elamite", "elx");

            lang_categories.add("English-based Creole or Pidgin");
            lang_map.put("English-based Creole or Pidgin", "cpe");
            lang_categories.add("Erzya");
            lang_map.put("Erzya", "myv");
            lang_categories.add("Esperanto");
            lang_map.put("Esperanto", "eo");
            lang_categories.add("Estonian");
            lang_map.put("Estonian", "et");
            lang_categories.add("Ewe");
            lang_map.put("Ewe", "ee");
            lang_categories.add("Ewondo");
            lang_map.put("Ewondo", "ewo");
            lang_categories.add("Fang");
            lang_map.put("Fang", "fan");
            lang_categories.add("Fanti");
            lang_map.put("Fanti", "fat");
            lang_categories.add("Faroese");
            lang_map.put("Faroese", "fo");
            lang_categories.add("Fijian");
            lang_map.put("Fijian", "fj");
            lang_categories.add("Filipino");
            lang_map.put("Filipino", "fil");
            lang_categories.add("Finnish");
            lang_map.put("Finnish", "fi");
            lang_categories.add("Finno-Ugrian Language");
            lang_map.put("Finno-Ugrian Language", "fiu");
            lang_categories.add("Flemish");
            lang_map.put("Flemish", "nl_BE");
            lang_categories.add("Fon");
            lang_map.put("Fon", "fon");
            lang_categories.add("French");
            lang_map.put("French", "fr");
            lang_categories.add("French-based Creole or Pidgin");
            lang_map.put("French-based Creole or Pidgin", "cpf");
            lang_categories.add("Friulian");
            lang_map.put("Friulian", "fur");
            lang_categories.add("Fulah");
            lang_map.put("Fulah", "ff");
            lang_categories.add("Ga");
            lang_map.put("Ga", "gaa");
            lang_categories.add("Galician");
            lang_map.put("Galician", "gl");
            lang_categories.add("Ganda");
            lang_map.put("Ganda", "lg");
            lang_categories.add("Gayo");
            lang_map.put("Gayo", "gay");
            lang_categories.add("Gbaya");
            lang_map.put("Gbaya", "gba");
            lang_categories.add("Geez");
            lang_map.put("Geez", "gez");
            lang_categories.add("Georgian");
            lang_map.put("Georgian", "ka");
            lang_categories.add("German");
            lang_map.put("German", "de");
            lang_categories.add("Germanic Language");
            lang_map.put("Germanic Language", "gem");
            lang_categories.add("Gilbertese");
            lang_map.put("Gilbertese", "gil");
            lang_categories.add("Gondi");
            lang_map.put("Gondi", "gon");
            lang_categories.add("Gorontalo");
            lang_map.put("Gorontalo", "gor");
            lang_categories.add("Gothic");
            lang_map.put("Gothic", "got");
            lang_categories.add("Grebo");
            lang_map.put("Grebo", "grb");
            lang_categories.add("Greek");
            lang_map.put("Greek", "el");
            lang_categories.add("Guarani");
            lang_map.put("Guarani", "gn");
            lang_categories.add("Gujarati");
            lang_map.put("Gujarati", "gu");
            lang_categories.add("Gwichʼin");
            lang_map.put("Gwichʼin", "gwi");
            lang_categories.add("Haida");
            lang_map.put("Haida", "hai");
            lang_categories.add("Haitian");
            lang_map.put("Haitian", "ht");
            lang_categories.add("Hausa");
            lang_map.put("Hausa", "ha");
            lang_categories.add("Hawaiian");
            lang_map.put("Hawaiian", "haw");
            lang_categories.add("Hebrew");
            lang_map.put("Hebrew", "he");
            lang_categories.add("Herero");
            lang_map.put("Herero", "hz");
            lang_categories.add("Hiligaynon");
            lang_map.put("Hiligaynon", "hil");
            lang_categories.add("Himachali");
            lang_map.put("Himachali", "him");

            lang_categories.add("Hiri Motu");
            lang_map.put("Hiri Motu", "ho");
            lang_categories.add("Hittite");
            lang_map.put("Hittite", "hit");
            lang_categories.add("Hmong");
            lang_map.put("Hmong", "hmn");
            lang_categories.add("Hungarian");
            lang_map.put("Hungarian", "hu");
            lang_categories.add("Hupa");
            lang_map.put("Hupa", "hup");
            lang_categories.add("Iban");
            lang_map.put("Iban", "iba");
            lang_categories.add("Iberian Portuguese");
            lang_map.put("Iberian Portuguese", "pt_PT");
            lang_categories.add("Iberian Spanish");
            lang_map.put("Iberian Spanish", "es_ES");
            lang_categories.add("Icelandic");
            lang_map.put("Icelandic", "is");
            lang_categories.add("Ido");
            lang_map.put("Ido", "io");
            lang_categories.add("Igbo");
            lang_map.put("Igbo", "ig");
            lang_categories.add("Ijo");
            lang_map.put("Ijo", "ijo");
            lang_categories.add("Iloko");
            lang_map.put("Iloko", "ilo");
            lang_categories.add("Inari Sami");
            lang_map.put("Inari Sami", "smn");
            lang_categories.add("Indic Language");
            lang_map.put("Indic Language", "inc");
            lang_categories.add("Indo-European Language");
            lang_map.put("Indo-European Language", "ine");
            lang_categories.add("Indonesian");
            lang_map.put("Indonesian", "id");
            lang_categories.add("Ingush");
            lang_map.put("Ingush", "inh");
            lang_categories.add("Interlingua");
            lang_map.put("Interlingua", "ia");
            lang_categories.add("Interlingue");
            lang_map.put("Interlingue", "ie");
            lang_categories.add("Inuktitut");
            lang_map.put("Inuktitut", "iu");
            lang_categories.add("Inupiaq");
            lang_map.put("Inupiaq", "ik");
            lang_categories.add("Iranian Language");
            lang_map.put("Iranian Language", "ira");
            lang_categories.add("Irish");
            lang_map.put("Irish", "ga");
            lang_categories.add("Iroquoian Language");
            lang_map.put("Iroquoian Language", "iro");
            lang_categories.add("Italian");
            lang_map.put("Italian", "it");
            lang_categories.add("Japanese");
            lang_map.put("Japanese", "ja");
            lang_categories.add("Javanese");
            lang_map.put("Javanese", "jv");
            lang_categories.add("Jju");
            lang_map.put("Jju", "kaj");
            lang_categories.add("Judeo-Arabic");
            lang_map.put("Judeo-Arabic", "jrb");
            lang_categories.add("Judeo-Persian");
            lang_map.put("Judeo-Persian", "jpr");
            lang_categories.add("Kabardian");
            lang_map.put("Kabardian", "kbd");
            lang_categories.add("Kabyle");
            lang_map.put("Kabyle", "kab");
            lang_categories.add("Kachin");
            lang_map.put("Kachin", "kac");
            lang_categories.add("Kalaallisut");
            lang_map.put("Kalaallisut", "kl");
            lang_categories.add("Kalmyk");
            lang_map.put("Kalmyk", "xal");
            lang_categories.add("Kamba");
            lang_map.put("Kamba", "kam");
            lang_categories.add("Kannada");
            lang_map.put("Kannada", "kn");
            lang_categories.add("Kanuri");
            lang_map.put("Kanuri", "kr");
            lang_categories.add("Karachay-Balkar");
            lang_map.put("Karachay-Balkar", "krc");
            lang_categories.add("Kara-Kalpak");
            lang_map.put("Kara-Kalpak", "kaa");
            lang_categories.add("Karelian");
            lang_map.put("Karelian", "krl");
            lang_categories.add("Karen");
            lang_map.put("Karen", "kar");
            lang_categories.add("Kashmiri");
            lang_map.put("Kashmiri", "ks");
            lang_categories.add("Kashubian");
            lang_map.put("Kashubian", "csb");
            lang_categories.add("Kawi");
            lang_map.put("Kawi", "kaw");
            lang_categories.add("Kazakh");
            lang_map.put("Kazakh", "kk");
            lang_categories.add("Khasi");
            lang_map.put("Khasi", "kha");
            lang_categories.add("Khmer");
            lang_map.put("Khmer", "km");
            lang_categories.add("Khoisan Language");
            lang_map.put("Khoisan Language", "khi");
            lang_categories.add("Khotanese");
            lang_map.put("Khotanese", "kho");
            lang_categories.add("Kikuyu");
            lang_map.put("Kikuyu", "ki");
            lang_categories.add("Kimbundu");
            lang_map.put("Kimbundu", "kmb");
            lang_categories.add("Kinyarwanda");
            lang_map.put("Kinyarwanda", "rw");
            lang_categories.add("Kirghiz");
            lang_map.put("Kirghiz", "ky");
            lang_categories.add("Klingon");
            lang_map.put("Klingon", "tlh");
            lang_categories.add("Komi");
            lang_map.put("Komi", "kv");
            lang_categories.add("Kongo");
            lang_map.put("Kongo", "kg");
            lang_categories.add("Konkani");
            lang_map.put("Konkani", "kok");
            lang_categories.add("Korean");
            lang_map.put("Korean", "ko");
            lang_categories.add("Koro");
            lang_map.put("Koro", "kfo");
            lang_categories.add("Kosraean");
            lang_map.put("Kosraean", "kos");
            lang_categories.add("Kpelle");
            lang_map.put("Kpelle", "kpe");
            lang_categories.add("Kru");
            lang_map.put("Kru", "kro");
            lang_categories.add("Kuanyama");
            lang_map.put("Kuanyama", "kj");
            lang_categories.add("Kumyk");
            lang_map.put("Kumyk", "kum");
            lang_categories.add("Kurdish");
            lang_map.put("Kurdish", "ku");
            lang_categories.add("Kurukh");
            lang_map.put("Kurukh", "kru");
            lang_categories.add("Kutenai");
            lang_map.put("Kutenai", "kut");
            lang_categories.add("Ladino");
            lang_map.put("Ladino", "lad");
            lang_categories.add("Lahnda");
            lang_map.put("Lahnda", "lah");
            lang_categories.add("Lamba");
            lang_map.put("Lamba", "lam");
            lang_categories.add("Lao");
            lang_map.put("Lao", "lo");
            lang_categories.add("Latin");
            lang_map.put("Latin", "la");
            lang_categories.add("Latin American Spanish");
            lang_map.put("Latin American Spanish", "es_419");
            lang_categories.add("Latvian");
            lang_map.put("Latvian", "lv");
            lang_categories.add("Lezghian");
            lang_map.put("Lezghian", "lez");
            lang_categories.add("Limburgish");
            lang_map.put("Limburgish", "li");
            lang_categories.add("Lingala");
            lang_map.put("Lingala", "ln");
            lang_categories.add("Lithuanian");
            lang_map.put("Lithuanian", "lt");
            lang_categories.add("Lojban");
            lang_map.put("Lojban", "jbo");
            lang_categories.add("Lower Sorbian");
            lang_map.put("Lower Sorbian", "dsb");
            lang_categories.add("Low German");
            lang_map.put("Low German", "nds");
            lang_categories.add("Lozi");
            lang_map.put("Lozi", "loz");
            lang_categories.add("Luba-Katanga");
            lang_map.put("Luba-Katanga", "lu");
            lang_categories.add("Luba-Lulua");
            lang_map.put("Luba-Lulua", "lua");
            lang_categories.add("Luiseno");
            lang_map.put("Luiseno", "lui");
            lang_categories.add("Lule Sami");
            lang_map.put("Lule Sami", "smj");
            lang_categories.add("Lunda");
            lang_map.put("Lunda", "lun");
            lang_categories.add("Luo");
            lang_map.put("Luo", "luo");
            lang_categories.add("Lushai");
            lang_map.put("Lushai", "lus");
            lang_categories.add("Luxembourgish");
            lang_map.put("Luxembourgish", "lb");
            lang_categories.add("Macedonian");
            lang_map.put("Macedonian", "mk");
            lang_categories.add("Madurese");
            lang_map.put("Madurese", "mad");
            lang_categories.add("Magahi");
            lang_map.put("Magahi", "mag");
            lang_categories.add("Maithili");
            lang_map.put("Maithili", "mai");
            lang_categories.add("Makasar");
            lang_map.put("Makasar", "mak");
            lang_categories.add("Malagasy");
            lang_map.put("Malagasy", "mg");
            lang_categories.add("Malay");
            lang_map.put("Malay", "ms");
            lang_categories.add("Malayalam");
            lang_map.put("Malayalam", "ml");
            lang_categories.add("Maltese");
            lang_map.put("Maltese", "mt");
            lang_categories.add("Manchu");
            lang_map.put("Manchu", "mnc");
            lang_categories.add("Mandar");
            lang_map.put("Mandar", "mdr");
            lang_categories.add("Mandingo");
            lang_map.put("Mandingo", "man");
            lang_categories.add("Manipuri");
            lang_map.put("Manipuri", "mni");
            lang_categories.add("Manobo Language");
            lang_map.put("Manobo Language", "mno");
            lang_categories.add("Manx");
            lang_map.put("Manx", "gv");
            lang_categories.add("Maori");
            lang_map.put("Maori", "mi");
            lang_categories.add("Marathi");
            lang_map.put("Marathi", "mr");
            lang_categories.add("Mari");
            lang_map.put("Mari", "chm");
            lang_categories.add("Marshallese");
            lang_map.put("Marshallese", "mh");
            lang_categories.add("Marwari");
            lang_map.put("Marwari", "mwr");
            lang_categories.add("Masai");
            lang_map.put("Masai", "mas");
            lang_categories.add("Mayan Language");
            lang_map.put("Mayan Language", "myn");
            lang_categories.add("Mende");
            lang_map.put("Mende", "men");
            lang_categories.add("Micmac");
            lang_map.put("Micmac", "mic");
            lang_categories.add("Middle Dutch");
            lang_map.put("Middle Dutch", "dum");
            lang_categories.add("Middle English");
            lang_map.put("Middle English", "enm");
            lang_categories.add("Middle French");
            lang_map.put("Middle French", "frm");
            lang_categories.add("Middle High German");
            lang_map.put("Middle High German", "gmh");
            lang_categories.add("Middle Irish");
            lang_map.put("Middle Irish", "mga");
            lang_categories.add("Minangkabau");
            lang_map.put("Minangkabau", "min");
            lang_categories.add("Mirandese");
            lang_map.put("Mirandese", "mwl");
            lang_categories.add("Miscellaneous Language");
            lang_map.put("Miscellaneous Language", "mis");
            lang_categories.add("Mohawk");
            lang_map.put("Mohawk", "moh");
            lang_categories.add("Moksha");
            lang_map.put("Moksha", "mdf");
            lang_categories.add("Moldavian");
            lang_map.put("Moldavian", "mo");
            lang_categories.add("Mongo");
            lang_map.put("Mongo", "lol");
            lang_categories.add("Mongolian");
            lang_map.put("Mongolian", "mn");
            lang_categories.add("Mon-Khmer Language");
            lang_map.put("Mon-Khmer Language", "mkh");
            lang_categories.add("Morisyen");
            lang_map.put("Morisyen", "mfe");
            lang_categories.add("Mossi");
            lang_map.put("Mossi", "mos");
            lang_categories.add("Multiple Languages");
            lang_map.put("Multiple Languages", "mul");
            lang_categories.add("Munda Language");
            lang_map.put("Munda Language", "mun");
            lang_categories.add("Nahuatl");
            lang_map.put("Nahuatl", "nah");
            lang_categories.add("Nauru");
            lang_map.put("Nauru", "na");
            lang_categories.add("Navajo");
            lang_map.put("Navajo", "nv");
            lang_categories.add("Ndonga");
            lang_map.put("Ndonga", "ng");
            lang_categories.add("Neapolitan");
            lang_map.put("Neapolitan", "nap");
            lang_categories.add("Nepali");
            lang_map.put("Nepali", "ne");
            lang_categories.add("Newari");
            lang_map.put("Newari", "new");
            lang_categories.add("Nias");
            lang_map.put("Nias", "nia");
            lang_categories.add("Niger-Kordofanian Language");
            lang_map.put("Niger-Kordofanian Language", "nic");
            lang_categories.add("Nilo-Saharan Language");
            lang_map.put("Nilo-Saharan Language", "ssa");
            lang_categories.add("Niuean");
            lang_map.put("Niuean", "niu");
            lang_categories.add("N’Ko");
            lang_map.put("N’Ko", "nqo");
            lang_categories.add("Nogai");
            lang_map.put("Nogai", "nog");
            lang_categories.add("No linguistic content");
            lang_map.put("No linguistic content", "zxx");
            lang_categories.add("North American Indian Language");
            lang_map.put("North American Indian Language", "nai");
            lang_categories.add("Northern Frisian");
            lang_map.put("Northern Frisian", "frr");
            lang_categories.add("Northern Sami");
            lang_map.put("Northern Sami", "se");
            lang_categories.add("Northern Sotho");
            lang_map.put("Northern Sotho", "nso");
            lang_categories.add("North Ndebele");
            lang_map.put("North Ndebele", "nd");
            lang_categories.add("Norwegian");
            lang_map.put("Norwegian", "no");
            lang_categories.add("Norwegian Bokmål");
            lang_map.put("Norwegian Bokmål", "nb");
            lang_categories.add("Norwegian Nynorsk");
            lang_map.put("Norwegian Nynorsk", "nn");
            lang_categories.add("Nubian Language");
            lang_map.put("Nubian Language", "nub");
            lang_categories.add("Nyamwezi");
            lang_map.put("Nyamwezi", "nym");
            lang_categories.add("Nyanja");
            lang_map.put("Nyanja", "ny");
            lang_categories.add("Nyankole");
            lang_map.put("Nyankole", "nyn");
            lang_categories.add("Nyasa Tonga");
            lang_map.put("Nyasa Tonga", "tog");
            lang_categories.add("Nyoro");
            lang_map.put("Nyoro", "nyo");
            lang_categories.add("Nzima");
            lang_map.put("Nzima", "nzi");
            lang_categories.add("Occitan");
            lang_map.put("Occitan", "oc");
            lang_categories.add("Ojibwa");
            lang_map.put("Ojibwa", "oj");
            lang_categories.add("Old English");
            lang_map.put("Old English", "ang");
            lang_categories.add("Old French");
            lang_map.put("Old French", "fro");
            lang_categories.add("Old High German");
            lang_map.put("Old High German", "goh");
            lang_categories.add("Old Irish");
            lang_map.put("Old Irish", "sga");
            lang_categories.add("Old Norse");
            lang_map.put("Old Norse", "non");
            lang_categories.add("Old Persian");
            lang_map.put("Old Persian", "peo");
            lang_categories.add("Old Provençal");
            lang_map.put("Old Provençal", "pro");
            lang_categories.add("Oriya");
            lang_map.put("Oriya", "or");
            lang_categories.add("Oromo");
            lang_map.put("Oromo", "om");
            lang_categories.add("Osage");
            lang_map.put("Osage", "osa");
            lang_categories.add("Ossetic");
            lang_map.put("Ossetic", "os");
            lang_categories.add("Otomian Language");
            lang_map.put("Otomian Language", "oto");
            lang_categories.add("Ottoman Turkish");
            lang_map.put("Ottoman Turkish", "ota");
            lang_categories.add("Pahlavi");
            lang_map.put("Pahlavi", "pal");
            lang_categories.add("Palauan");
            lang_map.put("Palauan", "pau");
            lang_categories.add("Pali");
            lang_map.put("Pali", "pi");
            lang_categories.add("Pampanga");
            lang_map.put("Pampanga", "pam");
            lang_categories.add("Pangasinan");
            lang_map.put("Pangasinan", "pag");
            lang_categories.add("Papiamento");
            lang_map.put("Papiamento", "pap");
            lang_categories.add("Papuan Language");
            lang_map.put("Papuan Language", "paa");
            lang_categories.add("Pashto");
            lang_map.put("Pashto", "ps");
            lang_categories.add("Persian");
            lang_map.put("Persian", "fa");
            lang_categories.add("Philippine Language");
            lang_map.put("Philippine Language", "phi");
            lang_categories.add("Phoenician");
            lang_map.put("Phoenician", "phn");
            lang_categories.add("Pohnpeian");
            lang_map.put("Pohnpeian", "pon");
            lang_categories.add("Polish");
            lang_map.put("Polish", "pl");
            lang_categories.add("Portuguese");
            lang_map.put("Portuguese", "pt");
            lang_categories.add("Portuguese-based Creole or Pidgin");
            lang_map.put("Portuguese-based Creole or Pidgin", "cpp");
            lang_categories.add("Prakrit Language");
            lang_map.put("Prakrit Language", "pra");
            lang_categories.add("Punjabi");
            lang_map.put("Punjabi", "pa");
            lang_categories.add("Quechua");
            lang_map.put("Quechua", "qu");
            lang_categories.add("Rajasthani");
            lang_map.put("Rajasthani", "raj");
            lang_categories.add("Rapanui");
            lang_map.put("Rapanui", "rap");
            lang_categories.add("Rarotongan");
            lang_map.put("Rarotongan", "rar");
            lang_categories.add("Rhaeto-Romance");
            lang_map.put("Rhaeto-Romance", "rm");
            lang_categories.add("Romance Language");
            lang_map.put("Romance Language", "roa");
            lang_categories.add("Romanian");
            lang_map.put("Romanian", "ro");
            lang_categories.add("Romany");
            lang_map.put("Romany", "rom");
            lang_categories.add("Root");
            lang_map.put("Root", "root");
            lang_categories.add("Rundi");
            lang_map.put("Rundi", "rn");
            lang_categories.add("Russian");
            lang_map.put("Russian", "ru");
            lang_categories.add("Salishan Language");
            lang_map.put("Salishan Language", "sal");
            lang_categories.add("Samaritan Aramaic");
            lang_map.put("Samaritan Aramaic", "sam");
            lang_categories.add("Sami Language");
            lang_map.put("Sami Language", "smi");
            lang_categories.add("Samoan");
            lang_map.put("Samoan", "sm");
            lang_categories.add("Sandawe");
            lang_map.put("Sandawe", "sad");
            lang_categories.add("Sango");
            lang_map.put("Sango", "sg");
            lang_categories.add("Sanskrit");
            lang_map.put("Sanskrit", "sa");
            lang_categories.add("Santali");
            lang_map.put("Santali", "sat");
            lang_categories.add("Sardinian");
            lang_map.put("Sardinian", "sc");
            lang_categories.add("Sasak");
            lang_map.put("Sasak", "sas");
            lang_categories.add("Scots");
            lang_map.put("Scots", "sco");
            lang_categories.add("Scottish Gaelic");
            lang_map.put("Scottish Gaelic", "gd");
            lang_categories.add("Selkup");
            lang_map.put("Selkup", "sel");
            lang_categories.add("Semitic Language");
            lang_map.put("Semitic Language", "sem");
            lang_categories.add("Serbian");
            lang_map.put("Serbian", "sr");
            lang_categories.add("Serbo-Croatian");
            lang_map.put("Serbo-Croatian", "sh");
            lang_categories.add("Serer");
            lang_map.put("Serer", "srr");
            lang_categories.add("Shan");
            lang_map.put("Shan", "shn");
            lang_categories.add("Shona");
            lang_map.put("Shona", "sn");
            lang_categories.add("Sichuan Yi");
            lang_map.put("Sichuan Yi", "ii");
            lang_categories.add("Sicilian");
            lang_map.put("Sicilian", "scn");
            lang_categories.add("Sidamo");
            lang_map.put("Sidamo", "sid");
            lang_categories.add("Sign Language");
            lang_map.put("Sign Language", "sgn");
            lang_categories.add("Siksika");
            lang_map.put("Siksika", "bla");
            lang_categories.add("Simplified Chinese");
            lang_map.put("Simplified Chinese", "zh_Hans");
            lang_categories.add("Sindhi");
            lang_map.put("Sindhi", "sd");
            lang_categories.add("Sinhala");
            lang_map.put("Sinhala", "si");
            lang_categories.add("Sino-Tibetan Language");
            lang_map.put("Sino-Tibetan Language", "sit");
            lang_categories.add("Siouan Language");
            lang_map.put("Siouan Language", "sio");
            lang_categories.add("Skolt Sami");
            lang_map.put("Skolt Sami", "sms");
            lang_categories.add("Slave");
            lang_map.put("Slave", "den");
            lang_categories.add("Slavic Language");
            lang_map.put("Slavic Language", "sla");
            lang_categories.add("Slovak");
            lang_map.put("Slovak", "sk");
            lang_categories.add("Slovenian");
            lang_map.put("Slovenian", "sl");
            lang_categories.add("Sogdien");
            lang_map.put("Sogdien", "sog");
            lang_categories.add("Somali");
            lang_map.put("Somali", "so");
            lang_categories.add("Songhai");
            lang_map.put("Songhai", "son");
            lang_categories.add("Soninke");
            lang_map.put("Soninke", "snk");
            lang_categories.add("Sorbian Language");
            lang_map.put("Sorbian Language", "wen");
            lang_categories.add("South American Indian Language");
            lang_map.put("South American Indian Language", "sai");
            lang_categories.add("Southern Altai");
            lang_map.put("Southern Altai", "alt");
            lang_categories.add("Southern Sami");
            lang_map.put("Southern Sami", "sma");
            lang_categories.add("Southern Sotho");
            lang_map.put("Southern Sotho", "st");
            lang_categories.add("South Ndebele");
            lang_map.put("South Ndebele", "nr");
            lang_categories.add("Spanish");
            lang_map.put("Spanish", "es");
            lang_categories.add("Sranan Tongo");
            lang_map.put("Sranan Tongo", "srn");
            lang_categories.add("Sukuma");
            lang_map.put("Sukuma", "suk");
            lang_categories.add("Sumerian");
            lang_map.put("Sumerian", "sux");
            lang_categories.add("Sundanese");
            lang_map.put("Sundanese", "su");
            lang_categories.add("Susu");
            lang_map.put("Susu", "sus");
            lang_categories.add("Swahili");
            lang_map.put("Swahili", "sw");
            lang_categories.add("Swati");
            lang_map.put("Swati", "ss");
            lang_categories.add("Swedish");
            lang_map.put("Swedish", "sv");
            lang_categories.add("Swiss French");
            lang_map.put("Swiss French", "fr_CH");
            lang_categories.add("Swiss German");
            lang_map.put("Swiss German", "gsw");
            lang_categories.add("Swiss High German");
            lang_map.put("Swiss High German", "de_CH");
            lang_categories.add("Syriac");
            lang_map.put("Syriac", "syr");
            lang_categories.add("Tagalog");
            lang_map.put("Tagalog", "tl");
            lang_categories.add("Tahitian");
            lang_map.put("Tahitian", "ty");
            lang_categories.add("Tai Language");
            lang_map.put("Tai Language", "tai");
            lang_categories.add("Tajik");
            lang_map.put("Tajik", "tg");
            lang_categories.add("Tamashek");
            lang_map.put("Tamashek", "tmh");
            lang_categories.add("Tamil");
            lang_map.put("Tamil", "ta");
            lang_categories.add("Taroko");
            lang_map.put("Taroko", "trv");
            lang_categories.add("Tatar");
            lang_map.put("Tatar", "tt");
            lang_categories.add("Telugu");
            lang_map.put("Telugu", "te");
            lang_categories.add("Tereno");
            lang_map.put("Tereno", "ter");
            lang_categories.add("Tetum");
            lang_map.put("Tetum", "tet");
            lang_categories.add("Thai");
            lang_map.put("Thai", "th");
            lang_categories.add("Tibetan");
            lang_map.put("Tibetan", "bo");
            lang_categories.add("Tigre");
            lang_map.put("Tigre", "tig");
            lang_categories.add("Tigrinya");
            lang_map.put("Tigrinya", "ti");
            lang_categories.add("Timne");
            lang_map.put("Timne", "tem");
            lang_categories.add("Tiv");
            lang_map.put("Tiv", "tiv");
            lang_categories.add("Tlingit");
            lang_map.put("Tlingit", "tli");
            lang_categories.add("Tokelau");
            lang_map.put("Tokelau", "tkl");
            lang_categories.add("Tok Pisin");
            lang_map.put("Tok Pisin", "tpi");
            lang_categories.add("Tonga");
            lang_map.put("Tonga", "to");
            lang_categories.add("Traditional Chinese");
            lang_map.put("Traditional Chinese", "zh_Hant");
            lang_categories.add("Tsimshian");
            lang_map.put("Tsimshian", "tsi");
            lang_categories.add("Tsonga");
            lang_map.put("Tsonga", "ts");
            lang_categories.add("Tswana");
            lang_map.put("Tswana", "tn");
            lang_categories.add("Tumbuka");
            lang_map.put("Tumbuka", "tum");
            lang_categories.add("Tupi Language");
            lang_map.put("Tupi Language", "tup");
            lang_categories.add("Turkish");
            lang_map.put("Turkish", "tr");
            lang_categories.add("Turkmen");
            lang_map.put("Turkmen", "tk");
            lang_categories.add("Tuvalu");
            lang_map.put("Tuvalu", "tvl");
            lang_categories.add("Tuvinian");
            lang_map.put("Tuvinian", "tyv");
            lang_categories.add("Twi");
            lang_map.put("Twi", "tw");
            lang_categories.add("Tyap");
            lang_map.put("Tyap", "kcg");
            lang_categories.add("Udmurt");
            lang_map.put("Udmurt", "udm");
            lang_categories.add("Ugaritic");
            lang_map.put("Ugaritic", "uga");
            lang_categories.add("Uighur");
            lang_map.put("Uighur", "ug");
            lang_categories.add("Ukrainian");
            lang_map.put("Ukrainian", "uk");
            lang_categories.add("Umbundu");
            lang_map.put("Umbundu", "umb");
            lang_categories.add("Unknown or Invalid Language");
            lang_map.put("Unknown or Invalid Language", "und");
            lang_categories.add("Upper Sorbian");
            lang_map.put("Upper Sorbian", "hsb");
            lang_categories.add("Urdu");
            lang_map.put("Urdu", "ur");
            lang_categories.add("U.S. English");
            lang_map.put("U.S. English", "en_US");
            lang_categories.add("Uzbek");
            lang_map.put("Uzbek", "uz");
            lang_categories.add("Vai");
            lang_map.put("Vai", "vai");
            lang_categories.add("Venda");
            lang_map.put("Venda", "ve");
            lang_categories.add("Vietnamese");
            lang_map.put("Vietnamese", "vi");
            lang_categories.add("Volapük");
            lang_map.put("Volapük", "vo");
            lang_categories.add("Votic");
            lang_map.put("Votic", "vot");
            lang_categories.add("Wakashan Language");
            lang_map.put("Wakashan Language", "wak");
            lang_categories.add("Walamo");
            lang_map.put("Walamo", "wal");
            lang_categories.add("Walloon");
            lang_map.put("Walloon", "wa");
            lang_categories.add("Waray");
            lang_map.put("Waray", "war");
            lang_categories.add("Washo");
            lang_map.put("Washo", "was");
            lang_categories.add("Welsh");
            lang_map.put("Welsh", "cy");
            lang_categories.add("Western Frisian");
            lang_map.put("Western Frisian", "fy");
            lang_categories.add("Wolof");
            lang_map.put("Wolof", "wo");
            lang_categories.add("Xhosa");
            lang_map.put("Xhosa", "xh");
            lang_categories.add("Yakut");
            lang_map.put("Yakut", "sah");
            lang_categories.add("Yao");
            lang_map.put("Yao", "yao");
            lang_categories.add("Yapese");
            lang_map.put("Yapese", "yap");
            lang_categories.add("Yiddish");
            lang_map.put("Yiddish", "yi");
            lang_categories.add("Yoruba");
            lang_map.put("Yoruba", "yo");
            lang_categories.add("Yupik Language");
            lang_map.put("Yupik Language", "ypk");
            lang_categories.add("Zande");
            lang_map.put("Zande", "znd");
            lang_categories.add("Zapotec");
            lang_map.put("Zapotec", "zap");
            lang_categories.add("Zaza");
            lang_map.put("Zaza", "zza");
            lang_categories.add("Zenaga");
            lang_map.put("Zenaga", "zen");
            lang_categories.add("Zhuang");
            lang_map.put("Zhuang", "za");
            lang_categories.add("Zulu");
            lang_map.put("Zulu", "zu");
            lang_categories.add("Zuni");
            lang_map.put("Zuni", "zun");

            ArrayAdapter<String> lang_dataAdapter = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, lang_categories);
            lang_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_lang.setAdapter(lang_dataAdapter);
            //---------------------------------------------
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cons_phno = edt_phoneno.getText().toString();
                if (!radio_id.equals("0")) {
                    if (!cons_phno.equals("")) {

                        if (new NetCheck().netcheck(Doc_Consultation1.this)) {
                            if (isSlotAvail == 1) {
                                String consultTime = null;
                                String timezone = spinner_timezone.getSelectedItem().toString();
//                            String  displaytime=spinner_timerange.getSelectedItem().toString();
                                for (int i = 0; i < timeSlotList.size(); i++) {
                                    if (spinner_timerange.getSelectedItem().toString().equals(timeSlotList.get(i).getDisplaytime())) {
                                        consultTime = String.valueOf(timeSlotList.get(i).getRailmin());
//                                        return;
                                        break;
                                    }
                                }
                                if (timezone == null || timezone.isEmpty() || timezone.length() == 0 || timezone.equals("") || timezone.equals("Choose another timezone")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select time zone and try again..", Toast.LENGTH_SHORT).show();
                                } else if (spinner_timerange.getSelectedItem().toString() == null || spinner_timerange.getSelectedItem().toString().isEmpty() || spinner_timerange.getSelectedItem().toString().length() < 0 || spinner_timerange.getSelectedItem().toString().equals("")|| spinner_timerange.getSelectedItem().toString().equals("Select Time Range")|| spinner_timerange.getSelectedItem().toString().equals("No Timeslot Available")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select time range and try again..", Toast.LENGTH_SHORT).show();
                                } else if (newDateStr == null || newDateStr.isEmpty() || newDateStr.length() == 0 || newDateStr.equals("")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select Valid Date and try again..", Toast.LENGTH_SHORT).show();
                                } else if (consultTime == null || consultTime.isEmpty() || consultTime.length() == 0 || consultTime.equals("")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select Valid Time and try again..", Toast.LENGTH_SHORT).show();
                                } else {
                                    submitConsultationWithSlots(timezone, spinner_timerange.getSelectedItem().toString(), consultTime);
                                }
                            } else if (isSlotAvail == 0) {
                                String timezone = spinner_timezone.getSelectedItem().toString();
//                            String  displaytime=spinner_timerange.getSelectedItem().toString();
//                            for (int i = 0; i < timeSlotList.size(); i++) {
//                                if (spinner_timerange.getSelectedItem().toString().equals(timeSlotList.get(i).getDisplaytime())) {
//                                    consulttime = String.valueOf(timeSlotList.get(i).getRailmin());
//                                    return;
//                                }
//                            }
                                if (timezone == null || timezone.isEmpty() || timezone.length() == 0 || timezone.equals("") || timezone.equals("Choose another timezone")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select time zone and try again..", Toast.LENGTH_SHORT).show();
                                } else if (spinner_timerange.getSelectedItem().toString() == null || spinner_timerange.getSelectedItem().toString().isEmpty() || spinner_timerange.getSelectedItem().toString().length() < 0 || spinner_timerange.getSelectedItem().toString().equals("")|| spinner_timerange.getSelectedItem().toString().equals("Select Time Range")|| spinner_timerange.getSelectedItem().toString().equals("No Timeslot Available")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select time range and try again..", Toast.LENGTH_SHORT).show();
                                } else if (newDateStr == null || newDateStr.isEmpty() || newDateStr.length() == 0 || newDateStr.equals("")) {
                                    Toast.makeText(Doc_Consultation1.this, "Please Select Valid Date and try again..", Toast.LENGTH_SHORT).show();
                                } else {
                                    submitWithConsultTime(timezone, spinner_timerange.getSelectedItem().toString());
                                }
                            }
                        } else {
                            Toast.makeText(Doc_Consultation1.this, "Please check your Internet Connection and try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your callback number.... ", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select Consultant Profile.. ", Toast.LENGTH_LONG).show();

                }

            }
        });
        isDocSlotAvailable("","");
        spinner_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(i==0)
                {
//                    Log.e("spinner_slot",spinner_timezone.getSelectedItem().toString()+" ");
                    getTimeSlot(spinner_slot.getSelectedItem().toString(),spinner_timezone.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        try {
//            //------------------------------------------------------------
//            String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&token=" + Model.token + "&enc=1";
//            System.out.println("url-------------" + url);
//            new JSON_Time_Range().execute(url);
//            //---------------------------------------------------
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        try {
//            //------------------------------------------------------------
//            String url = Model.BASE_URL + "/sapp/isDocSlotAvailable?format=json_new&user_id="+Model.id+"&token=" + Model.token +"&os_type=android&doctor_id="+Model.doctor_id +"&tz_name=&target_date="+"&enc=1";
////            sapp/isDocSlotAvailable?user_id=[user_id]&token=[token]&os_type=[android/ios]&doctor_id=[doctor_id]&tz_name=[patient_time_zone]&target_date=[YYYY/MM/DD]
//            System.out.println("slot-------------" + url);
//            Log.e("Sloturl",url+" ");
//            new JSON_IsDocSlotAvailable().execute(url);
//            //---------------------------------------------------
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //------------------------------------------------------------------------------------
        spinner_ccode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_ccode.getSelectedView();
                cc_name = textView.getText().toString();
                cccode = cc_map.get(cc_name);

                sel_country_code = cccode;
                sel_country_name = cc_name;

                System.out.println("sel_country_code------" + sel_country_code);
                System.out.println("sel_country_name------" + sel_country_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //------------------------------------------------------------------------------------
        spinner_timezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_timezone.getSelectedView();
                spintz = textView.getText().toString();
                System.out.println("spintz 1---------" + spintz);
                if (spintz.equalsIgnoreCase("Choose another timezone")) {
                    System.out.println("spintz 2---------" + spintz);
                    Intent intent = new Intent(Doc_Consultation1.this, TimeZoneActivity.class);
                    startActivity(intent);
                }

//            Log.e("spinnertime select",spinner_timezone.getSelectedItem().toString()+" ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                TextView textView = (TextView) spinner_timezone.getSelectedView();
//                spintz = textView.getText().toString();
//                System.out.println("spintz in nothing seletece  1---------" + spintz);
//                if (spintz.equalsIgnoreCase("Choose another timezone")) {
//                    System.out.println("spintz in nothing seletece   2---------" + spintz);
//                    Intent intent = new Intent(Doc_Consultation1.this, TimeZoneActivity.class);
//                    startActivity(intent);
//                }

            }
        });
        spinner_timezone.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                Log.e("spinner_timezone",spinner_timezone.getSelectedItem().toString()+"  ");
                final int MAKE_A_SELECTION = 0; //whatever index that is the normal starting point of the spinner.
                spinner_timezone.setSelection(MAKE_A_SELECTION);
                return false;
            }
        });
//        getTimeSlot();

        //------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------
        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) spinner_lang.getSelectedView();
                lang_name = textView.getText().toString();
                lang_val = lang_map.get(lang_name);

                Model.cons_lang = lang_name;
                Model.cons_lang_code = lang_val;

                System.out.println("Model.cons_lang----------" + Model.cons_lang);
                System.out.println("Model.cons_lang_code----------" + Model.cons_lang_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //------------------------------------------------------------------------------------
        spinner_timerange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    TextView textView = (TextView) spinner_timerange.getSelectedView();
                    sel_timerange = textView.getText().toString();
                    sel_timerange_code = timerange_map.get(sel_timerange);
                    Model.sel_timerange_code = timerange_map.get(sel_timerange);

                    Model.time_range = sel_timerange;
                    System.out.println("Model.time_range------" + Model.time_range);
                    System.out.println("Sel_timerange_code------" + Model.sel_timerange_code);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------
        tv_changetimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Doc_Consultation1.this, TimeZoneActivity.class);
                startActivity(intent);
            }
        });
        //----------------------------------------------------------------------------------
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(Doc_Consultation1.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                        cons_select_date = selectedyear + "/" + (selectedmonth + 1) + "/" + selectedday;
//                        Log.e("cons_select_date", cons_select_date + " ");
                        //--------- for System -------------------
                        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
                        Date dateObj = null;
                        try {
                            dateObj = curFormater.parse(cons_select_date);
                            newDateStr = curFormater.format(dateObj);
//                            Log.e("newDateStr", newDateStr + " ");
                            btn_date.setText(newDateStr);
                            isDocSlotAvailable(newDateStr, spinner_timezone.getSelectedItem().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //--------------------------------
                    }
                }, mYear, mMonth, mDay);

//                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                if (isSlotAvail==1){
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                }else if (isSlotAvail==0){
                    mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis()+1000*60*60*24);
                }
                mDatePicker.show();
            }
        });


        //-----------------------------------------------------------------
//        btn_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    Calendar now = Calendar.getInstance();
//                    DatePickerDialog dpd = DatePickerDialog.newInstance(
//                            Doc_Consultation1.this,
//                            now.get(Calendar.YEAR),
//                            now.get(Calendar.MONTH),
//                            now.get(Calendar.DAY_OF_MONTH)
//                    );
//                    dpd.setThemeDark(false);
//                    dpd.vibrate(true);
//                    dpd.dismissOnPause(false);
//                    dpd.showYearPickerFirst(false);
//                    dpd.setAccentColor(Color.parseColor("#9C27B0"));
//                    dpd.setTitle("DatePicker Title");
//                    //--------------------------------------
//
//                    //---------Limit Dates-----------------------------
//                    Calendar[] lim_dates = new Calendar[300];
//                    for (int i = 1; i <= 300; i++) {
//                        Calendar date = Calendar.getInstance();
//                        date.add(Calendar.DATE, i);
//                        lim_dates[i - 1] = date;
//                    }
//                    dpd.setSelectableDays(lim_dates);
//                    //---------Limit Dates-----------------------------
//
//                    //--------------------------------------
//                    Calendar[] dates = new Calendar[13];
//                    for (int i = -6; i <= 6; i++) {
//                        Calendar date = Calendar.getInstance();
//                        date.add(Calendar.WEEK_OF_YEAR, i);
//                        dates[i + 6] = date;
//                    }
//                    dpd.setHighlightedDays(dates);
//                    //--------------------------------------
//
//                    dpd.show(getFragmentManager(), "Datepickerdialog");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });

    }


    private void submitWithConsultTime(String timezone, String displaytime) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//        String timezone=spinner_timezone.getSelectedItem().toString();
//        displaytime=spinner_timerange.getSelectedItem().toString();
//        for(int i=0;i<timeSlotList.size();i++){
//            if (spinner_timerange.getSelectedItem().toString().equals(timeSlotList.get(i).getDisplaytime())){
//                consulttime= String.valueOf(timeSlotList.get(i).getRailmin());
//            }
//        }

        JsonObject post_json = new JsonObject();
        post_json.addProperty("user_id", (Model.id));
        post_json.addProperty("doctor_id", Doc_id);
        post_json.addProperty("consult_type", cons_type);
        post_json.addProperty("query", Query);
        post_json.addProperty("consult_date", cons_select_date);
        post_json.addProperty("lang", lang_val);
        post_json.addProperty("book_type", "booking");
        post_json.addProperty("timezone", timezone);
        post_json.addProperty("fp_id", radio_id);
        post_json.addProperty("time_range", displaytime);
        post_json.addProperty("ccode", cccode);
        post_json.addProperty("number", cons_phno);
        post_json.addProperty("speciality", "0");
//        Log.e("submitWithConsultTime",post_json.toString()+" ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        service.submitWithConsultTime(Model.id,  Model.token,"android",post_json ).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.e("submitConsultationWit", response+ "  getTimeSlot");
                progressDialog.dismiss();
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    if (response!=null) {
                        String json = String.valueOf(response.body());
                        try {
                            JSONObject jsonObj = new JSONObject(json);
//                                Log.e("submitConsultationWit",jsonObj.toString()+"  ");
                            String status=jsonObj.getString("status");
                            if (status.equals("0")){
                                if (jsonObj.has("msg")) {
                                    String msg = jsonObj.getString("msg");
                                    AlertBoxMethod(msg);
                                }
                            }else if (status.equals("1")){
                                id=jsonObj.getString("id");
                                book_type=jsonObj.getString("book_type");
//                                    Log.e("book_type",book_type+"  ");
                                Intent intent = new Intent(Doc_Consultation1.this, Consultation3.class);
                                intent.putExtra("consult_id", id);
                                intent.putExtra("item_type", book_type);
                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        Doc_Consultation1.this.finish();
                                    }
                                });
                                startActivityForResult(intent, 1);
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitConsultationWithSlots(String timezone, String display_time, String consult_time) {


        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        JsonObject post_json = new JsonObject();
        post_json.addProperty("user_id", (Model.id));
        post_json.addProperty("doctor_id", Doc_id);
        post_json.addProperty("consult_type", cons_type);
        post_json.addProperty("query", Query);
        post_json.addProperty("consult_date", cons_select_date);
        post_json.addProperty("lang", lang_val);
        post_json.addProperty("book_type", "appointment");
        post_json.addProperty("consult_time", consult_time);
        post_json.addProperty("consult_display_time", display_time);
        post_json.addProperty("timezone", timezone);
        post_json.addProperty("fp_id", radio_id);
//        Log.e("post_json",post_json.toString()+" ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
//        Log.e("service",service.submitConsultationWithSlots(Model.id, Model.token,"android",post_json).request().url()+" ");
        service.submitConsultationWithSlots(Model.id,  Model.token,"android",post_json).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.e("submitConsult", response.code()+ "  getTimeSlot");
                progressDialog.dismiss();
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    if (response!=null) {
                        String json = String.valueOf(response.body());
                        try {
                            JSONObject jsonObj = new JSONObject(json);
//                            Log.e("submitConsultationWit",jsonObj.toString()+"  ");

                            String status=jsonObj.getString("status");
                            if (status.equals("0")){
                                if (jsonObj.has("msg")) {
                                    String msg = jsonObj.getString("msg");
                                    AlertBoxMethod(msg);
                                }
                            }else if (status.equals("1")) {
                                id = jsonObj.getString("id");
                                book_type = jsonObj.getString("book_type");
//                                    Log.e("book_type in doc", book_type+ "  doc");
//                                    Log.e("id in doc", id+ "  doc");
                                Intent intent = new Intent(Doc_Consultation1.this, Consultation3.class);
                                intent.putExtra("consult_id", id);
                                intent.putExtra("item_type", book_type);
                                intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                                    @Override
                                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                                        Doc_Consultation1.this.finish();
                                    }
                                });
                                startActivityForResult(intent, 1);
                            }


                        } catch(JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //first API
//../sapp/isDocSlotAvailable?user_id=[user_id]&token=[token]&os_type=[android/ios]&doctor_id=[doctor_id]&tz_name=[patient_time_zone]&target_date=[YYYY/MM/DD]


    //response
//    "{""isSlotAvail"":1, ""slot_type"":""am/pm"", ""content"":[{""railmin"":"""",""displaytime"":""""},...], ""timezone"":"""", ""status"":1, ""target_date"":""""}
//
//    {""status"":0, ""msg"":""""}
//
//if isSlotAvail is 1 means: show the slot details and hide the time range
//
//if isSlotAvail is 0 means: hide the slot details and show the time range"


    //Second Api
//    ../sapp/getTimeSlots?user_id=[user_id]&token=[token]&os_type=[android/ios]&doctor_id=[doctor_id]&slot_type=[am/pm]&target_date=[YYYY/MM/DD]

//response
//{"slot_type":"am/pm", "content":[{"railmin":"","displaytime":""},...], "status":1, "target_date":""}


    private void getTimeSlot(String slot_type, String timeZone) {
//                Log.e("timeZone",timeZone+" ");
        String newTimeZone = null;
        try {
            newTimeZone = new String(timeZone.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        Log.e("newTimeZone in slot",newTimeZone+" ");
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait..");
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
//        Log.e("service",service.getTimeSlots(Model.id,"android " ,Model.token,Doc_id,slot_type, btn_date.getText().toString(),newTimeZone).request().url()+" ");
        service.getTimeSlots(Model.id, "android ", Model.token, Doc_id, slot_type, btn_date.getText().toString(),newTimeZone).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.e("gettimezone response", response.code()+ "  getTimeSlot");

                dialog.dismiss();
                if (response.code() == 200) {
                    dialog.dismiss();
                    if (response.body() != null) {
                        String json = String.valueOf(response.body());
                        JSONObject object= null;
                        try {
                            object = new JSONObject(json);
//                            Log.e("object",object.toString()+" ");
                            if (object.has("token_status")) {
                                String token_status = object.getString("token_status");
                                if (token_status.equals("0")) {
                                    finishAffinity();
                                    Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                String jsonInString=object.getString("status");

                                if (jsonInString.equals("0")) {
                                    if (object.has("msg")) {
                                        String msg=object.getString("msg");
                                        final List<String> categories_tr = new ArrayList<String>();
                                        categories_tr.add("No Timeslot Available");
                                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner_timerange.setAdapter(dataAdapter2);
                                        AlertBoxMethod(msg);
                                    }
                                }else{
                                    timeSlotList.clear();
                                    Gson gson = new Gson();
                                    SlotTypeModel user = gson.fromJson(object.toString(), SlotTypeModel.class);
//                                    Log.e("gettimezone", user.getSlotType().toString() + "  ");
                                    timeSlotList.addAll(user.getContent());
                                    final List<String> categories_tr = new ArrayList<String>();
                                    categories_tr.clear();
                                    categories_tr.add("Select Time Range");
                                    for (Content content : timeSlotList) {
                                        categories_tr.add(content.getDisplaytime());
                                    }
                                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_timerange.setAdapter(dataAdapter2);
                                }

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }


                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AlertBoxMethod(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> arg0.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//         class JSON_IsDocSlotAvailable extends AsyncTask<String, Void, String> {
//
//            ProgressDialog dialog;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                dialog = new ProgressDialog(Doc_Consultation1.this);
//                dialog.setMessage("Please wait");
//                dialog.show();
//                dialog.setCancelable(false);
//
//            }
//
//            @Override
//            protected String doInBackground(String... urls) {
//
//                try {
//
//                    JSONParser jParser = new JSONParser();
//                    String timeslot = jParser.getJSONString(urls[0]);
//                    Log.e("timeslot",timeslot.toString()+"  ");
//
//                    return timeslot;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            protected void onPostExecute(String timeslot) {
//
//             Log.e("timeslot",timeslot.toString()+"  ");
//
//                  dialog.dismiss();
//            }
//        }

    //    }
    private void isDocSlotAvailable(String date, String timeZone) {
//        Log.e("is doc","working");
//      Log.e("timeZone in slot",timeZone+" ");
        String newTimeZone = null;
        try {
            newTimeZone = new String(timeZone.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//      Log.e("newTimeZone in slot",newTimeZone+" ");
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
//      Log.e("isDocSlotAvailable",service.isDocSlotAvailable(Model.id,"android ",Model.token,Doc_id,newTimeZone,date,"1").request().url()+" ");

        service.isDocSlotAvailable(Model.id,"android ",Model.token,Doc_id,newTimeZone,date,"1").enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JSONObject object = null;
                if (response.code() == 200) {
                    String json = String.valueOf(response.body());
                    dialog.dismiss();
                    try {
                        object = new JSONObject(json);
//                        Log.e("object in isslot", object.toString() + " ");
                        if (object.has("token_status")) {
                            String token_status = object.getString("token_status");
                            if (token_status.equals("0")) {
                                finishAffinity();
                                Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            String jsonInString = object.getString("status");
                            if (jsonInString.equals("0")) {
                                if (object.has("msg")) {
                                    String msg=object.getString("msg");
                                    spinner_timerange.setVisibility(View.GONE);
                                    spinner_slot.setVisibility(View.GONE);
                                    final List<String> categories2 = new ArrayList<String>();
                                    final List<String> categories_tr = new ArrayList<String>();
                                    categories_tr.add("No Timeslot Available");
                                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories2);
                                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_slot.setAdapter(dataAdapter1);
                                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_timerange.setAdapter(dataAdapter2);
                                    AlertBoxMethod(msg);
                                }
                            } else {
                                Gson gson = new Gson();
                                TimeslotModel user = gson.fromJson(object.toString(), TimeslotModel.class);
                                String target_date = object.getString("target_date");


                                if (target_date!=null && !target_date.equals("")){
                                    btn_date.setText(target_date);
                                }else{
                                    btn_date.setText("Please select Date");
                                }

                                isSlotAvail = user.getIsSlotAvail();
                                if (user.getIsSlotAvail() == 1) {
                                    spinner_timerange.setVisibility(View.VISIBLE);
                                    spinner_slot.setVisibility(View.VISIBLE);
                                    final List<String> cat = new ArrayList<String>();
                                    cat.add("am");
                                    cat.add("pm");
                                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, cat);
                                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_slot.setAdapter(dataAdapter1);
                                    i = 0;
                                    if (user.getSlotType().equalsIgnoreCase("am")) {
                                        spinner_slot.setSelection(0);
                                    } else if (user.getSlotType().equalsIgnoreCase("pm")) {
                                        spinner_slot.setSelection(1);
                                    }
                                    if (user.getTimezone() != null) {
                                        final List<String> categories2 = new ArrayList<String>();
                                        categories2.clear();
                                        categories2.add(user.getTimezone());
                                        categories2.add("Choose another timezone");
                                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories2);
                                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner_timezone.setAdapter(dataAdapter2);
                                    }
                                    if (user.getContent() != null) {
                                        timeSlotList.clear();
                                        timeSlotList.addAll(user.getContent());
                                        final List<String> categories_tr = new ArrayList<String>();
                                        categories_tr.clear();
                                        categories_tr.add("Select Time Range");
                                        for (Content content : timeSlotList) {
                                            categories_tr.add(content.getDisplaytime());
                                        }
                                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner_timerange.setAdapter(dataAdapter2);
                                    }
                                } else {

                                    dialog.dismiss();
                                    i = 1;


                                    try {
                                        //------------------------------------------------------------
                                        String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&tz_name=" + timeZone + "&token=" + Model.token + "&enc=1";
                                        System.out.println("url-------------" + url);
                                        new JSON_Time_Range().execute(url);
                                        //---------------------------------------------------

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
//        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
//        if (dpd != null) dpd.setOnDateSetListener(this);

        //--------------------------------------------------
        if ((Model.doc_consult_time) != null && !(Model.doc_consult_time).isEmpty() && !(Model.doc_consult_time).equals("null") && !(Model.doc_consult_time).equals("")) {
            try {
                //----------------------------
//                String url = Model.BASE_URL + "/sapp/getTimeRange?format=json_new&tz_name=" + (Model.cons_timezone_val) + "&token=" + Model.token + "&enc=1";
//                System.out.println("getTimerange url--------" + url);
//                new JSON_Time_Range().execute(url);
//                    Log.e("newDateStr in resume",newDateStr+" ");
//                    Log.e("consulttime in resume",Model.doc_consult_time+" ");
//                    Log.e("consultzone in resume",Model.doc_consult_zone+" ");
                isDocSlotAvailable(newDateStr, Model.doc_consult_time);
                Model.doc_consult_time="";
                Model.doc_consult_zone="";
                //----------------------------
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //--------------------------------------------------
//        try {
//            //------------------------------------------------------------
//            String url = Model.BASE_URL + "/sapp/isDocSlotAvailable?format=json_new&user_id="+Model.id+"&token=" + Model.token +"&os_type=android"+"&doctor_id="+Model.doctor_id +"&tz_name="+"&target_date="+"&enc=1";
////            sapp/isDocSlotAvailable?user_id=[user_id]&token=[token]&os_type=[android/ios]&doctor_id=[doctor_id]&tz_name=[patient_time_zone]&target_date=[YYYY/MM/DD]
//            System.out.println("slot-------------" + url);
//            Log.e("Sloturl",url+" ");
//            new JSON_IsDocSlotAvailable().execute(url);
//            //---------------------------------------------------
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        try {
            String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            String secondString = second < 10 ? "0" + second : "" + second;
            String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
            timeTextView.setText(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//
//        try {
//            //String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
//            cons_select_date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
//            System.out.println("Cal Date------" + cons_select_date);
//
//            //--------- for System -------------------
//            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy/MM/dd");
//            Date dateObj = curFormater.parse(cons_select_date);
//             newDateStr = curFormater.format(dateObj);
//            System.out.println("For System  select_date---------" + newDateStr);
//            //--------------------------------
//            btn_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//            isDocSlotAvailable(newDateStr, spinner_timezone.getSelectedItem().toString());
//            System.out.println("cons_select_date---------" + cons_select_date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    class JSONPostQuery extends AsyncTask<JSONObject, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Submitting, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(JSONObject... urls) {
            try {
                JSONParser jParser = new JSONParser();
                cons_booking_jsonobj = jParser.JSON_POST(urls[0], "Submit_consultation");

                System.out.println("cons_booking_jsonobj---------------" + cons_booking_jsonobj.toString());
                System.out.println("Cons_Booking_params--------------" + urls[0]);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            try {

                if (cons_booking_jsonobj.has("token_status")) {
                    String token_status = cons_booking_jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        finishAffinity();
                        Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    consult_id = cons_booking_jsonobj.getString("id");
                    Model.consult_id = consult_id;
                    Model.qid = consult_id;

                    System.out.println("consult_id-----------" + consult_id);

                    Model.query_launch = "Consultation2";

                    Intent intent = new Intent(Doc_Consultation1.this, Consultation3.class);
                    intent.putExtra("consult_id", consult_id);
                    intent.putExtra("finisher", new android.os.ResultReceiver(null) {
                        @Override
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            Doc_Consultation1.this.finish   ();
                        }
                    });
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                      dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class JSON_Time_Range extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                jsonobj = jParser.getJSONFromUrl(urls[0]);
                System.out.println("Time_Range jsonobj-----" + jsonobj.toString());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {

            final List<String> categories_tr = new ArrayList<String>();

            try {

                if (jsonobj.has("token_status")) {
                    String token_status = jsonobj.getString("token_status");
                    if (token_status.equals("0")) {
                        //============================================================
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Login_Status, "0");
                        editor.apply();
                        //============================================================
                        finishAffinity();
                        Intent intent = new Intent(Doc_Consultation1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    content_str = jsonobj.getString("content");
                    timezone_str = jsonobj.getString("timezone");

                    Model.cons_timezone = timezone_str;

                    System.out.println("content_str--------" + content_str);
                    System.out.println("timezone_str--------" + timezone_str);
                    System.out.println("Model.cons_timezone--------" + Model.cons_timezone);

                    JSONObject content_obj = new JSONObject(content_str);
                    String fone = content_obj.getString("1");
                    System.out.println("fone--------" + fone);

                    JSONArray jsonarray = new JSONArray();
                    jsonarray.put(content_obj);
                    timerange_map.clear();
                    categories_tr.clear();
                    categories_tr.add("Select Time Range");
                    timerange_map.put(times_values, "0");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobj1c = jsonarray.getJSONObject(i);
                        System.out.println("jsonobj1c-----" + jsonobj1c.toString());

                        for (int j = 1; j <= 24; j++) {

                            if (jsonobj1c.has("" + j)) {
                                times_values = jsonobj1c.getString("" + j);
                                //======================================================================
                                categories_tr.add(times_values);
                                timerange_map.put(times_values, "" + j);
                                System.out.println("times_values-----" + times_values);
                                System.out.println("times_values---j---" + j);
                                //=========================================================
                            }
                        }

                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories_tr);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_timerange.setAdapter(dataAdapter2);
                    }


                    //----------- Time Zone ------------------------
                    final List<String> categories2 = new ArrayList<String>();
                    categories2.add(timezone_str);
                    categories2.add("Choose another timezone");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Doc_Consultation1.this, android.R.layout.simple_spinner_item, categories2);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_timezone.setAdapter(dataAdapter2);
                    //----------- Time Zone ------------------------
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

              dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void post_consultation() {

        if ((Model.id) != null && !(Model.id).isEmpty() && !(Model.id).equals("null") && !(Model.id).equals("")) {

            System.out.println("Model.id-----" + Model.id);
            System.out.println("Model.const_type-----" + cons_type);
            System.out.println("Model.cons_query-----" + Query);
            System.out.println("Model.time_range-----" + sel_timerange_code);
            System.out.println("Model.cons_select_date-----" + cons_select_date);
            System.out.println("Model.cons_lang-----" + lang_val);
            System.out.println("Model.cons_ccode-----" + sel_country_code);
            System.out.println("Model.cons_number-----" + cons_phno);
            System.out.println("Model.cons_timezone-----" + Model.cons_timezone);
            System.out.println("Doc_id-----" + Doc_id);

            try {
                cccode = "" + countryCodePicker.getSelectedCountryCodeAsInt();
                System.out.println("Start cccode-----" + cccode);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                post_json = new JSONObject();
                post_json.put("user_id", (Model.id));
                post_json.put("doctor_id", Doc_id);
                post_json.put("consult_type", cons_type);
                post_json.put("query", Query);
                post_json.put("time_range", sel_timerange_code);
                post_json.put("consult_date", cons_select_date);
                post_json.put("lang", lang_val);
                post_json.put("ccode", cccode);
                post_json.put("number", cons_phno);
                post_json.put("timezone", (Model.cons_timezone));
                post_json.put("fp_id", radio_id);
                post_json.put("speciality", "0");

                System.out.println("post_json-------------" + post_json.toString());

                String date_text = btn_date.getText().toString();

                TextView textView = (TextView) spinner_timerange.getSelectedView();
                timerange_name = textView.getText().toString();

                //----------- Flurry -------------------------------------------------
                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put("android.patient.consult_type", cons_type);
                articleParams.put("android.patient.Query", Query);
                articleParams.put("android.patient.time_range", cons_type);
                articleParams.put("android.patient.Time_Range", sel_timerange_code);
                articleParams.put("android.patient.Consult_date", cons_select_date);
                articleParams.put("android.patient.Country_code", cccode);
                articleParams.put("android.patient.Ph_number", cons_phno);
                articleParams.put("android.patient.timezone", (Model.cons_timezone));
                articleParams.put("android.patient.speciality", "0");
                FlurryAgent.logEvent("android.patient.Direct_Consultation_Post", articleParams);
                //----------- Flurry -------------------------------------------------

                //------------ Google firebase Analitics-----------------------------------------------
                Model.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle params = new Bundle();
                params.putString("User", Model.id);
                params.putString("consult_type", cons_type);
                params.putString("Consult_date", cons_select_date);
                Model.mFirebaseAnalytics.logEvent("Direct_Consultation_Post", params);
                //------------ Google firebase Analitics-----------------------------------------------

                if (!date_text.equals("Select Date")) {
                    if (!timerange_name.equals("Select Time Range")) {
                        new JSONPostQuery().execute(post_json);
                    } else
                        Toast.makeText(getApplicationContext(), "Select Time Range for consultation", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Select Consultation Date", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            force_logout();
        }
    }

    public void force_logout() {

        try {
            final MaterialDialog alert = new MaterialDialog(Doc_Consultation1.this);
            alert.setTitle("Oops..!");
            alert.setMessage("Something went wrong. Please go back and try again..!e");
            alert.setCanceledOnTouchOutside(false);
            alert.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //============================================================
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Login_Status, "0");
                    editor.apply();
                    //============================================================

                    finishAffinity();
                    Intent i = new Intent(Doc_Consultation1.this, LoginActivity.class);
                    startActivity(i);
                    alert.dismiss();
                    finish();
                }
            });
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class JSON_getFamily extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                family_list = jParser.getJSONString(urls[0]);
                System.out.println("Family URL---------------" + urls[0]);

                return family_list;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            apply_relaships_radio(family_list);

              dialog.dismiss();
        }
    }

    public void apply_relaships_radio(String fam_string) {

        try {

            parent_layout.removeAllViews();
            relation_type_val = "";

            JSONArray jaaray = new JSONArray(fam_string);

            RadioGroup ll = new RadioGroup(Doc_Consultation1.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            System.out.println("family_list.length--------- " + fam_string.length());

            if (fam_string.length() > 5) {

                for (int i = -1; i < (jaaray.length()) + 1; i++) {

                    RadioButton rdbtn = new RadioButton(Doc_Consultation1.this);
                    rdbtn.setId(((jaaray.length()) * 2) + i);
                    rdbtn.setButtonDrawable(null);
                    rdbtn.setTextColor(getResources().getColor(R.color.grey_800));
                    rdbtn.setTextSize(14);
                    rdbtn.setGravity(Gravity.CENTER);

                    //drawable.setBounds(0, 0, width, height);

                    //--------------- Margin ----------------
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    rdbtn.setLayoutParams(params);
                    //--------------- Margin ----------------

                    System.out.println("jaaray.length()-----------" + jaaray.length());
                    System.out.println("i-----------" + i);

                    if (i == -1) {
                        Drawable drawable = getResources().getDrawable(R.drawable.someone_button);
                        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
                        rdbtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                        rdbtn.setText("Myself");
                        //rdbtn.setChecked(true);
                    } else if (i == jaaray.length()) {
                        Drawable drawable = getResources().getDrawable(R.drawable.someone_else_button);
                        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
                        rdbtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                        rdbtn.setText("Someone else");
                    } else {
                        //--------- Getting Response Values ----------------------
                        JSONObject jsonobj1 = jaaray.getJSONObject(i);
                        System.out.println("jsonobj1------------" + jsonobj1.toString());
                        String id_val = jsonobj1.getString("id");
                        relation_type_val = jsonobj1.getString("relation_type");
                        String name_val = jsonobj1.getString("name");
                        String gender_val = jsonobj1.getString("gender");
                        //--------- Getting Response Values ----------------------

                        System.out.println("relation_type_val---- : + relation_type_val");

                        if (relation_type_val.equals("Myself")) {
                            myself_id = id_val;
                            System.out.println("Inner myself_id---- :" + myself_id);
                        } else {
                            // myself_id = "";
                            System.out.println("Else myself_id---- :" + myself_id);
                        }

                        family_map.put(name_val, id_val);

                        Drawable drawable = getResources().getDrawable(R.drawable.someone_button);
                        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
                        rdbtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

                        rdbtn.setText(name_val);
                        rdbtn.setMaxEms(5);
                        rdbtn.setEllipsize(TextUtils.TruncateAt.END);
                        rdbtn.setMaxLines(1);
                    }

                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RadioButton r1 = v.findViewById(v.getId());

                            r1.setSelected(true);

                            String rad_name = r1.getText().toString();
                            System.out.println("rad_name--------------------" + rad_name);

                            radio_id = family_map.get(rad_name);
                            System.out.println("radio_id-----" + radio_id);

                            switch (rad_name) {
                                case "Someone else": {

                                    //ask_someone("someone");

                                    Intent intent = new Intent(Doc_Consultation1.this, SomeoneEdit_Dialog.class);
                                    intent.putExtra("add_type", "someone");
                                    intent.putExtra("profile_id", "0");
                                    startActivity(intent);

                                    radio_id = "0";

                                    //buttonClicked();
                                    break;
                                }
                                case "Myself": {

                                    if (myself_id != null && !myself_id.isEmpty() && !myself_id.equals("null") && !myself_id.equals("")) {
                                        radio_id = myself_id;
                                        System.out.println("radio_id-----" + radio_id);

                                    } else {
                                        //ask_someone("myself");

                                        Intent intent = new Intent(Doc_Consultation1.this, SomeoneEdit_Dialog.class);
                                        intent.putExtra("add_type", "myself");
                                        intent.putExtra("profile_id", "0");
                                        startActivity(intent);
                                    }

                                    break;
                                }
                            }

                            //-------------------------------------------------------------------
                            if (!radio_id.equals("0")) {
                                String get_family_profile = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&id=" + radio_id + "&isView=1&token=" + Model.token;
                                System.out.println("get_family_profile---------" + get_family_profile);
                                new Doc_Consultation1.JSON_getFamDetails().execute(get_family_profile);
                                //---------------------------------------------------------------
                            }
                        }
                    });

                    System.out.println("relation_type_val------------ " + relation_type_val);

                    if (!relation_type_val.equals("Myself")) {
                        ll.addView(rdbtn);
                    } else {
                        System.out.println("Ohhhh..... MySelf------------");
                        relation_type_val = "";
                    }

/*                    if (!relation_type_val.equals("Myself")) {
                        ll.addView(rdbtn);
                        relation_type_val = "";
                        System.out.println("MySelf is here-------------------");
                    }*/

                }

                parent_layout.addView(ll);

            } else {

                System.out.println("Else part-------------------");

                for (int i = 0; i < 2; i++) {

                    RadioButton rdbtn = new RadioButton(Doc_Consultation1.this);
                    rdbtn.setId(((jaaray.length()) * 2) + i);
                    rdbtn.setButtonDrawable(null);
                    rdbtn.setTextColor(getResources().getColor(R.color.grey_800));
                    rdbtn.setTextSize(14);
                    rdbtn.setGravity(Gravity.CENTER);

                    //--------------- Margin ----------------
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    rdbtn.setLayoutParams(params);
                    //--------------- Margin ----------------

                    if (i == 0) {
                        rdbtn.setText("Myself");
                        Drawable drawable = getResources().getDrawable(R.drawable.someone_button);
                        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
                        rdbtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                        //rdbtn.setChecked(true);
                    }
                    if (i == 1) {
                        rdbtn.setText("Someone else");
                        Drawable drawable = getResources().getDrawable(R.drawable.someone_else_button);
                        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
                        rdbtn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    }


                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RadioButton r1 = v.findViewById(v.getId());

                            String rad_name = r1.getText().toString();
                            System.out.println("rad_name--------------------" + rad_name);

                            switch (rad_name) {
                                case "Someone else": {
                                    //ask_someone("someone");

                                    Intent intent = new Intent(Doc_Consultation1.this, SomeoneEdit_Dialog.class);
                                    intent.putExtra("add_type", "someone");
                                    startActivity(intent);


                                    break;
                                }
                                case "Myself": {
                                    //  ask_someone("myself");

                                    Intent intent = new Intent(Doc_Consultation1.this, SomeoneEdit_Dialog.class);
                                    intent.putExtra("add_type", "myself");
                                    startActivity(intent);

                                    break;
                                }
                            }

                            //-------------------------------------------------------------------
                            if (!radio_id.equals("0")) {
                                String get_family_profile = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&id=" + radio_id + "&isView=1&token=" + Model.token;
                                System.out.println("get_family_profile---------" + get_family_profile);
                                new Doc_Consultation1.JSON_getFamDetails().execute(get_family_profile);
                                //---------------------------------------------------------------
                            }

                        }
                    });


                    ll.addView(rdbtn);
                }

                parent_layout.addView(ll);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JSON_getFamDetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Please wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                famDets_text = jParser.getJSONString(urls[0]);

                System.out.println("Family URL- DEts--------------" + urls[0]);

                return famDets_text;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {
                System.out.println("family_list---------------" + family_list);

                JSONObject fam_obj = new JSONObject(family_list);

                System.out.println("fam_obj--------------" + fam_obj.toString());

                String name_text = fam_obj.getString("name");
                String relation_type_text = fam_obj.getString("relation_type");
                String age_text = fam_obj.getString("age");
                //String dob_text = fam_obj.getString("dob");
                String gender_text = fam_obj.getString("gender");
                String height_text = fam_obj.getString("height");
                String weight_text = fam_obj.getString("weight");

                System.out.println("Get name_text----------------" + name_text);
                //System.out.println("Get dob_text----------------" + dob_text);
                System.out.println("Get relation_type_text----------------" + relation_type_text);
                System.out.println("Get  age_text----------------" + age_text);
                System.out.println("gender_text----------------" + gender_text);
                System.out.println("height_text----------------" + height_text);
                System.out.println("weight_text----------------" + weight_text);


                if (relation_type_text != null && !relation_type_text.isEmpty() && !relation_type_text.equals("null") && !relation_type_text.equals("")) {
                } else {
                    relation_type_text = "";
                }
                if (age_text != null && !age_text.isEmpty() && !age_text.equals("null") && !age_text.equals("")) {
                } else {
                    age_text = "";

                }
                if (gender_text != null && !gender_text.isEmpty() && !gender_text.equals("null") && !gender_text.equals("")) {
                } else {
                    gender_text = "";
                }
                if (height_text != null && !height_text.isEmpty() && !height_text.equals("null") && !height_text.equals("")) {
                } else {
                    height_text = "";
                }
                if (weight_text != null && !weight_text.isEmpty() && !weight_text.equals("null") && !weight_text.equals("")) {
                } else {
                    weight_text = "";
                }
                if (name_text != null && !name_text.isEmpty() && !name_text.equals("null") && !name_text.equals("")) {
                } else {
                    name_text = "";
                }


                tv_fam_name.setText(name_text);
                tv_fam_agedets.setText(relation_type_text + " - " + gender_text);
                tv_fam_height.setText("Height : " + height_text);
                tv_fam_weight.setText("Weight : " + weight_text);


                try {
                    if (gender_text != null && !gender_text.isEmpty() && !gender_text.equals("null") && !gender_text.equals("")) {


                    } else {
                        //-------------------------------------------------------------------
                        String get_family_profile = Model.BASE_URL + "sapp/getFamilyProfile?user_id=" + Model.id + "&id=" + radio_id + "&isView=0&token=" + Model.token;
                        System.out.println("get_family_profile---------" + get_family_profile);
                        new Doc_Consultation1.JSON_EditFamDetails().execute(get_family_profile);
                        //---------------------------------------------------------------
                        Toast.makeText(Doc_Consultation1.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("radio_id=============" + radio_id);

                if (!radio_id.equals("0")) {
                    //-------- View --------------------------------------
                    famidets_layout.setVisibility(View.VISIBLE);
                    img_downarrow.setVisibility(View.GONE);
                    img_uparrow.setVisibility(View.VISIBLE);
                    //-------- View --------------------------------------
                } else {
                    famidets_layout.setVisibility(View.GONE);
                    img_downarrow.setVisibility(View.VISIBLE);
                    img_uparrow.setVisibility(View.GONE);
                    //-------- View --------------------------------------
                }

                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private class JSON_EditFamDetails extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Doc_Consultation1.this);
            dialog.setMessage("Please wait");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                JSONParser jParser = new JSONParser();
                famDets_text = jParser.getJSONString(urls[0]);

                System.out.println("Family URL- Dets--------------" + urls[0]);

                return famDets_text;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String family_list) {

            try {

                JSONObject fam_obj = new JSONObject(family_list);

                System.out.println("fam_obj--------------" + fam_obj.toString());

                rel_val = fam_obj.getString("relation_type");
                tit_id = fam_obj.getString("title_id");
                mem_name = fam_obj.getString("name");
                age_val = fam_obj.getString("age");
                dob_val = fam_obj.getString("dob");
                gender_val = fam_obj.getString("gender");
                height_val = fam_obj.getString("height");
                weight_val = fam_obj.getString("weight");

                tit_val = tit_id;

                System.out.println("tit_id-1--------------" + tit_id);
                System.out.println("mem_name 1---------------" + mem_name);
                System.out.println("rel_type 1---------------" + rel_val);
                System.out.println("age_val 1---------------" + age_val);
                System.out.println("dob_val 1---------------" + dob_val);
                System.out.println("gender_val- 1--------------" + gender_val);
                System.out.println("height_val-1--------------" + height_val);
                System.out.println("weight_val---------------" + weight_val);

                //ask_someone("edit");

                Intent intent = new Intent(Doc_Consultation1.this, SomeoneEdit_Dialog.class);
                intent.putExtra("add_type", "edit");
                intent.putExtra("profile_id", radio_id);
                intent.putExtra("tit_id", tit_id);
                intent.putExtra("mem_name", mem_name);
                intent.putExtra("rel_val", rel_val);
                intent.putExtra("age_val", age_val);
                intent.putExtra("dob_val", dob_val);
                intent.putExtra("gender_val", gender_val);
                intent.putExtra("height_val", height_val);
                intent.putExtra("weight_val", weight_val);
                startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
            }

              dialog.dismiss();
        }
    }




}
