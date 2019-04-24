package com.example.emailmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.emailmanager.account.EmailCategoryActivity;
import com.example.emailmanager.emails.InboxFragment;
import com.example.emailmanager.msgsend.SendMsgActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMsgActivity.start2SendMsgActivity(MainActivity.this, SendMsgActivity.SEND);
            }
        });

        ((TextView) headerView.findViewById(R.id.textView)).setText(EMApplication.getAccount().getAccount());
        headerView.findViewById(R.id.tv_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailCategoryActivity.start2EmailCategoryActivity(MainActivity.this);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Bundle bundle = new Bundle();
        InboxFragment inboxFragment = new InboxFragment();
        bundle.putInt(InboxFragment.FLAG, InboxFragment.INBOX);
        inboxFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_contain, inboxFragment).commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        InboxFragment inboxFragment = new InboxFragment();
        if (id == R.id.nav_inbox) {
            bundle.putInt(InboxFragment.FLAG, InboxFragment.INBOX);
        } else if (id == R.id.nav_drafts) {
            bundle.putInt(InboxFragment.FLAG, InboxFragment.DRAFTS);
        } else if (id == R.id.nav_send) {
            bundle.putInt(InboxFragment.FLAG, InboxFragment.SENT_MESSAGES);
        } else if (id == R.id.nav_delete) {

        }
        inboxFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_contain, inboxFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void start2MainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
