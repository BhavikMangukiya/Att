package com.players.bmef;

import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
/*import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAds.FinishState;
import com.unity3d.ads.UnityAds.UnityAdsError;*/
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OnClickListener {
    final int REQUEST_CODE = 170;
    private AdView adView;
    MyAdapter adapter;
    TextView advertisetext;
    final String app_id = BuildConfig.APPLICATION_ID;
    CardView cardView;
    CoordinatorLayout coordinatorLayout;
    LinearLayout empty_view;
    private InterstitialAd interstitialAd;
    LinearLayoutManager llm;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    LinearLayout scheduleLayout;
    Toolbar toolbar;
    RecyclerView view;

    class C02342 implements OnClickListener {

        class C02331 implements OnClickListener {
            C02331() {
            }

            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("market://details?id=com.alienartsoftwares.smartattendance"));
                HomeActivity.this.startActivity(i);
            }
        }

        C02342() {
        }
////////////////////////////////////////////////////  SUBJECT VADHARVA MMATE  --------------<<<<<<<<<<<<<<<<<<<<<<<
        public void onClick(View v) {
            if (new DataHandler(HomeActivity.this).getSubjectsCount() >= 5) {
                Snackbar.make(v, (int) R.string.cant_add, 0).setAction((int) R.string.buy_now, new C02331()).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("preactivity", "ManageSubjects");
            Intent intent = new Intent(HomeActivity.this, AddSubject.class);
            intent.putExtras(bundle);
            HomeActivity.this.startActivityForResult(intent, 101);
        }
    }

    class C02364 implements DialogInterface.OnClickListener {
        C02364() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02386 implements DialogInterface.OnClickListener {
        C02386() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class LoadRecyclerView extends AsyncTask<Void, Void, Void> {

        class C02411 implements Runnable {
            C02411() {
            }

            public void run() {
                List<Subject> list = new DataHandler(HomeActivity.this).getAllSubjects2();
                HomeActivity.this.adapter = new MyAdapter(list, HomeActivity.this);
                HomeActivity.this.view.setLayoutManager(HomeActivity.this.llm);
                HomeActivity.this.view.setAdapter(HomeActivity.this.adapter);
                if (list.isEmpty()) {
                    HomeActivity.this.empty_view.setVisibility(View.VISIBLE);
                    HomeActivity.this.view.setVisibility(View.INVISIBLE);
                    return;
                }
                HomeActivity.this.empty_view.setVisibility(View.INVISIBLE);
                HomeActivity.this.view.setVisibility(View.VISIBLE);
            }
        }

        LoadRecyclerView() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            HomeActivity.this.runOnUiThread(new C02411());
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class C08779 implements OnNavigationItemSelectedListener {
        C08779() {
        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.my_schedule:
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, ShowSchedule.class));
                    break;
                case R.id.promo:
                   // if (!UnityAds.isReady()) {
                        Toast.makeText(HomeActivity.this, HomeActivity.this.getResources().getString(R.string.advertise), Toast.LENGTH_LONG).show();
                        break;
                   // }
                   // UnityAds.show(HomeActivity.this);
                    //break;
                case R.id.rate:
                    Intent i = new Intent("android.intent.action.VIEW");
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                    HomeActivity.this.startActivity(i);
                    break;
                case R.id.pro:
                    Intent i2 = new Intent("android.intent.action.VIEW");
                    i2.setData(Uri.parse(""));
                    HomeActivity.this.startActivity(i2);
                    break;
                case R.id.about_us:
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, ActivityAbout.class));
                    break;
            }
            return true;
        }
    }

    class C12711 extends AbstractAdListener {
        C12711() {
        }

        public void onAdLoaded(Ad ad) {
            super.onAdLoaded(ad);
            HomeActivity.this.advertisetext.setVisibility(View.GONE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.home_activity);
        if (VERSION.SDK_INT >= 23) {
            int read_access = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
            int write_access = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            int internet = ContextCompat.checkSelfPermission(this, "android.permission.INTERNET");
            int boot = ContextCompat.checkSelfPermission(this, "android.permission.RECEIVE_BOOT_COMPLETED");
            if (read_access == 0 && write_access == 0 && internet == 0 && boot == 0) {
                initAd();
                switchChange();
                startService(new Intent(this, AppService.class));
                initUI();
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.INTERNET", "android.permission.RECEIVE_BOOT_COMPLETED"}, 170);
            return;
        }
        initAd();
        switchChange();
        startService(new Intent(this, AppService.class));
        initUI();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 170:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "App can't proceed without your permissions", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                initAd();
                switchChange();
                startService(new Intent(this, AppService.class));
                initUI();
                return;
            default:
                return;
        }
    }

    private void initAd() {
        this.cardView = (CardView) findViewById(R.id.adCard);
        this.advertisetext = (TextView) findViewById(R.id.advertise);
        this.cardView.setVisibility(View.VISIBLE);
        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
       // this.adView = new AdView(this, "174788086356667_175688269599982", AdSize.BANNER_320_50);
      //  adViewContainer.addView(this.adView);
      ///  this.adView.setAdListener(new C12711());
      //  this.adView.loadAd();
     //   this.interstitialAd = new InterstitialAd(this, "174788086356667_175740086261467");
    //    this.interstitialAd.loadAd();
      //  UnityAds.initialize(this, "1324460", this);
    }

    private void initUI() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
            getSupportActionBar().setTitle((int) R.string.my_subject);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator((int) R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        this.scheduleLayout = (LinearLayout) findViewById(R.id.schedule_layout);
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.navigationView.inflateMenu(R.menu.drawer_menu);
        if (this.navigationView != null) {
            setupDrawerContent(this.navigationView);
        }
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new C02342());
        this.view = (RecyclerView) findViewById(R.id.recyclerView);
        this.empty_view = (LinearLayout) findViewById(R.id.empty);
        this.llm = new LinearLayoutManager(this);
        new LoadRecyclerView().execute(new Void[0]);
    }

  /*  public void onUnityAdsReady(String s) {
    }

    public void onUnityAdsStart(String s) {
    }

    public void onUnityAdsFinish(String s, FinishState finishState) {
    }

    public void onUnityAdsError(UnityAdsError unityAdsError, String s) {
    }
*/
    protected void onDestroy() {
        super.onDestroy();
      //  if (this.interstitialAd.isAdLoaded()) {
      //      this.interstitialAd.show();
      //  }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            List<Subject> list = new DataHandler(this).getAllSubjects2();
            if (list.isEmpty()) {
                this.empty_view.setVisibility(View.VISIBLE);
                this.view.setVisibility(View.INVISIBLE);
            } else {
                this.empty_view.setVisibility(View.INVISIBLE);
                this.view.setVisibility(View.VISIBLE);
            }
            int result = data.getIntExtra("result", 0);
            int position = data.getIntExtra("position", 0);
            if (result == 1) {
                System.out.println(this.adapter.getItemCount() + "\t" + list.size());
                this.adapter.subjects.add(this.adapter.getItemCount(), list.get(list.size() - 1));
                this.adapter.notifyItemInserted(list.size() - 1);
                startService(new Intent(this, AppService.class));
                Snackbar.make(this.coordinatorLayout, (int) R.string.subject_saved, -1).show();
            } else {
                this.adapter.subjects.set(position, list.get(position));
                this.adapter.notifyDataSetChanged();
                this.adapter.notifyItemChanged(position);
                startService(new Intent(this, AppService.class));
                Snackbar.make(this.coordinatorLayout, (int) R.string.subject_updated, -1).show();
            }
        }
        if (resultCode == -1 && requestCode == 102) {
            Snackbar.make(this.coordinatorLayout, (int) R.string.attendance_saved, -1).show();
        }
    }

    public void onClick(final View v) {
        View view3 = (View) ((View) ((View) v.getParent()).getParent()).getParent();
        final int id = view3.getId();
        ContentValues values = new DataHandler(this).getSubjectInfo(id);
        Bundle basket = new Bundle();
        basket.putInt("id", values.getAsInteger("id").intValue());
        basket.putInt("roll_from", values.getAsInteger("roll_from").intValue());
        basket.putInt("roll_to", values.getAsInteger("roll_to").intValue());
        Intent intent;
        Builder dialog;
        switch (v.getId()) {
            case R.id.takeAttendance:
                intent = new Intent(this, AttendanceActivity.class);
                basket.putString("preactivity", "ShowSubjects");
                intent.putExtras(basket);
                startActivityForResult(intent, 102);
                return;
            case R.id.viewRecords:
                intent = new Intent(this, ShowRecords.class);
                intent.putExtras(basket);
                startActivity(intent);
                return;
            case R.id.edit:
                dialog = new Builder(this);
                dialog.setTitle(R.string.update);
                dialog.setMessage(R.string.update_proceed);
                dialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", id);
                        bundle.putString("preactivity", "UpdateSubject");
                        Intent intent = new Intent(HomeActivity.this, AddSubject.class);
                        intent.putExtra("position", ((View) v.getParent()).getId());
                        intent.putExtras(bundle);
                        HomeActivity.this.startActivityForResult(intent, 101);
                    }
                });
                dialog.setPositiveButton(R.string.no, new C02386());
                dialog.show();
                return;
            case R.id.delete:
                dialog = new Builder(this);
                dialog.setTitle(R.string.delete);
                dialog.setMessage(R.string.delete_proceed);
                dialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new DataHandler(HomeActivity.this).deleteSubject(id);
                            Snackbar.make(v, (int) R.string.subject_deleted, 0).show();
                            int position = ((View) v.getParent()).getId();
                            HomeActivity.this.adapter.subjects.remove(position);
                            HomeActivity.this.adapter.notifyItemRemoved(position);
                            HomeActivity.this.adapter.notifyItemRangeChanged(position, HomeActivity.this.adapter.subjects.size());
                            if (HomeActivity.this.adapter.subjects.isEmpty()) {
                                HomeActivity.this.empty_view.setVisibility(View.VISIBLE);
                                HomeActivity.this.view.setVisibility(View.INVISIBLE);
                                return;
                            }
                            HomeActivity.this.empty_view.setVisibility(View.INVISIBLE);
                            HomeActivity.this.view.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Toast.makeText(HomeActivity.this, "Something went wrong! :-(", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setPositiveButton(R.string.no, new C02364());
                dialog.show();
                return;
            case R.id.excl:
                LinearLayout l = (LinearLayout) view3.findViewById(R.id.scheduleLayout);
                if (l.isShown()) {
                    collapse(l);
                    v.setBackgroundResource(R.drawable.expand_selector);
                    return;
                }
                expand(l);
                v.setBackgroundResource(R.drawable.collapse_selector);
                return;
            default:
                return;
        }
    }

    public static void expand(final View v) {
        v.measure(-1, -2);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (((float) targetHeight) * interpolatedTime);
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) targetHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(View.GONE);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (((float) initialHeight) * interpolatedTime));
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) initialHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }

    private void setLocale(String s, int code) {
        Locale locale = new Locale(s);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        if (code == 1) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void switchChange() {
        String lang = getPreferences(0).getString("lang", "English");
        int i = -1;
        switch (lang.hashCode()) {
            case -1791347022:
                if (lang.equals("Marathi")) {
                    i = 1;
                    break;
                }
                break;
            case 60895824:
                if (lang.equals("English")) {
                    i = 0;
                    break;
                }
                break;
            case 69730482:
                if (lang.equals("Hindi")) {
                    i = 2;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                setLocale("en", 0);
                return;
            case 1:
                setLocale("mr", 0);
                return;
            case 2:
                setLocale("hi", 0);
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
/*    public boolean onCreateOptionsMenu(android.view.Menu r9) {
        *//*
        r8 = this;
        r4 = 0;
        r6 = 1;
        r5 = r8.getMenuInflater();
        r7 = 2131623939; // 0x7f0e0003 float:1.8875044E38 double:1.053162158E-314;
        r5.inflate(r7, r9);
        r3 = r8.getPreferences(r4);
        r5 = "lang";
        r7 = "English";
        r2 = r3.getString(r5, r7);
        r5 = 2131558677; // 0x7f0d0115 float:1.8742677E38 double:1.0531299144E-314;
        r0 = r9.findItem(r5);
        r5 = -1;
        r7 = r2.hashCode();
        switch(r7) {
            case -1791347022: goto L_0x0039;
            case 60895824: goto L_0x0030;
            case 69730482: goto L_0x0043;
            default: goto L_0x0027;
        };
    L_0x0027:
        r4 = r5;
    L_0x0028:
        switch(r4) {
            case 0: goto L_0x004d;
            case 1: goto L_0x0058;
            case 2: goto L_0x0063;
            default: goto L_0x002b;
        };
    L_0x002b:
        r4 = super.onCreateOptionsMenu(r9);
        return r4;
    L_0x0030:
        r7 = "English";
        r7 = r2.equals(r7);
        if (r7 == 0) goto L_0x0027;
    L_0x0038:
        goto L_0x0028;
    L_0x0039:
        r4 = "Marathi";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x0027;
    L_0x0041:
        r4 = r6;
        goto L_0x0028;
    L_0x0043:
        r4 = "Hindi";
        r4 = r2.equals(r4);
        if (r4 == 0) goto L_0x0027;
    L_0x004b:
        r4 = 2;
        goto L_0x0028;
    L_0x004d:
        r4 = 2131558678; // 0x7f0d0116 float:1.8742679E38 double:1.053129915E-314;
        r1 = r9.findItem(r4);
        r1.setChecked(r6);
        goto L_0x002b;
    L_0x0058:
        r4 = 2131558679; // 0x7f0d0117 float:1.874268E38 double:1.0531299154E-314;
        r1 = r9.findItem(r4);
        r1.setChecked(r6);
        goto L_0x002b;
    L_0x0063:
        r4 = 2131558680; // 0x7f0d0118 float:1.8742683E38 double:1.053129916E-314;
        r1 = r9.findItem(r4);
        r1.setChecked(r6);
        goto L_0x002b;
        *//*
        throw new UnsupportedOperationException("Method not decompiled: com.alienartsoftwares.smartattendance.HomeActivity.onCreateOptionsMenu(android.view.Menu):boolean");
    }*/

    public boolean onOptionsItemSelected(MenuItem item) {
        Editor editor = getPreferences(0).edit();
        switch (item.getItemId()) {
            case 16908332:
                this.mDrawerLayout.openDrawer((int) GravityCompat.START);
                return true;
            case R.id.cEnglish:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    editor.putString("lang", "English");
                    Snackbar.make(this.coordinatorLayout, (CharSequence) "Language changed to English", -1).show();
                    setLocale("en", 1);
                    editor.commit();
                    break;
                }
                break;
            case R.id.cMarathi:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    editor.putString("lang", "Marathi");
                    Snackbar.make(this.coordinatorLayout, (CharSequence) "Language changed to Marathi", -1).show();
                    setLocale("mr", 1);
                    editor.commit();
                    break;
                }
                break;
            case R.id.cHindi:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    editor.putString("lang", "Hindi");
                    Snackbar.make(this.coordinatorLayout, (CharSequence) "Language changed to Hindi", -1).show();
                    setLocale("hi", 1);
                    editor.commit();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new C08779());
    }
}
