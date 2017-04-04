package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.wootric.androidsdk.Constants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.ref.WeakReference;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class PreferencesUtilsTest {

    PreferencesUtils preferencesUtils;

    @Mock
    SharedPreferences prefs;

    @Mock
    Context context;

    @Mock
    WeakReference weakReference;

    @Before
    public void before() throws Exception {
        this.context = Mockito.mock(Context.class);
        this.weakReference = Mockito.mock(WeakReference.class);
        this.prefs = Mockito.mock(SharedPreferences.class);
        preferencesUtils = new PreferencesUtils(weakReference);
        Mockito.when(weakReference.get()).thenReturn(context);
        Mockito.when(context.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(prefs);
    }

    @Test
    public void touchLastSurveyed_whenResponse() throws Exception {
        SharedPreferences.Editor mockedEditor = Mockito.mock(SharedPreferences.Editor.class);
        Mockito.when(prefs.edit()).thenReturn(mockedEditor);
        preferencesUtils.touchLastSurveyed(true, 90);
        Mockito.verify(mockedEditor).apply();
    }

    @Test
    public void wasRecentlySurveyed_whenResponseAndSurveyedDefault() throws Exception {
        long now = new Date().getTime();

        Mockito.when(prefs.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(prefs.getString(Mockito.eq("type"), Mockito.anyString())).thenReturn("response");
        Mockito.when(prefs.getInt(Mockito.eq("resurvey_days"), Mockito.anyInt())).thenReturn(-1);

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());


        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS * 32L);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());


        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS *  92L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());
    }

    @Test
    public void wasRecentlySurveyed_whenResponseAndSurveyedCustomThrottle() throws Exception {
        long now = new Date().getTime();

        Mockito.when(prefs.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(prefs.getString(Mockito.eq("type"), Mockito.anyString())).thenReturn("response");
        Mockito.when(prefs.getInt(Mockito.eq("resurvey_days"), Mockito.anyInt())).thenReturn(30);

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS * 32L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());


        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS *  92L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());
    }

    @Test
    public void wasRecentlySurveyed_whenDeclineAndSurveyedDefault() throws Exception {
        long now = new Date().getTime();

        Mockito.when(prefs.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(prefs.getString(Mockito.eq("type"), Mockito.anyString())).thenReturn("decline");
        Mockito.when(prefs.getInt(Mockito.eq("resurvey_days"), Mockito.anyInt())).thenReturn(-1);

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS * 32L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());


        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS *  92L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());
    }

    @Test
    public void wasRecentlySurveyed_whenDeclineAndSurveyedCustomThrottle() throws Exception {
        long now = new Date().getTime();

        Mockito.when(prefs.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(prefs.getString(Mockito.eq("type"), Mockito.anyString())).thenReturn("decline");
        Mockito.when(prefs.getInt(Mockito.eq("resurvey_days"), Mockito.anyInt())).thenReturn(60);

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS * 32L);
        Assert.assertEquals(true, preferencesUtils.wasRecentlySurveyed());

        Mockito.when(prefs.getLong(Mockito.eq("surveyed"), Mockito.anyLong())).thenReturn(now - Constants.DAY_IN_MILLIS *  92L);
        Assert.assertEquals(false, preferencesUtils.wasRecentlySurveyed());
    }
}
