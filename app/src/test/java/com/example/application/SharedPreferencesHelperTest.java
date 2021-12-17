package com.example.application;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

@RunWith(MockitoJUnitRunner.class)
public class SharedPreferencesHelperTest {

    private static final String TEST_NAME = "Test name";

    private static final String TEST_EMAIL = "test@email.com";

    private static final Calendar TEST_DATE_OF_BIRTH = Calendar.getInstance();

    static

    {
        TEST_DATE_OF_BIRTH.set(1998,01,17);
    }

    private SharedPreferenceEntry  mSharedPreferenceEntry;

    private SharedPreferencesHelper mMockSharedPreferencesHelper;

    private SharedPreferencesHelper mMockBrokenSharedPreferencesHelper;

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences mMockBrokenSharedPreferences;

    @Mock
    SharedPreferences.Editor mMockEditor;

    @Mock
    SharedPreferences.Editor mMockBrokenEditor;

    @Before
    public void initMocks() {
        mSharedPreferenceEntry = new SharedPreferenceEntry(TEST_NAME, TEST_DATE_OF_BIRTH, TEST_EMAIL);

        mMockSharedPreferencesHelper = createMockSharedPreference();

        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference();

    }

    @Test
    public void sharedPreferencesHelper_SaveAndReadPersonalInformation() {

        boolean success = mMockSharedPreferencesHelper.savePersonalInfo(mSharedPreferenceEntry);

        assertThat("Checking that SharedPreferenceEntry.save... returns true",
                success, is(true));

        SharedPreferenceEntry savedSharedPreferenceEntry =
                mMockSharedPreferencesHelper.getPersonalInfo();

        assertThat("Checking that SharedPreferenceEntry.name has been persisted and read correctly",
                mSharedPreferenceEntry.getName(),
                is(equalTo(savedSharedPreferenceEntry.getName())));
        assertThat("Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                        + "correctly",
                mSharedPreferenceEntry.getDateOfBirth(),
                is(equalTo(savedSharedPreferenceEntry.getDateOfBirth())));
        assertThat("Checking that SharedPreferenceEntry.email has been persisted and read "
                        + "correctly",
                mSharedPreferenceEntry.getEmail(),
                is(equalTo(savedSharedPreferenceEntry.getEmail())));
    }
    @Test
    public void sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {

        boolean success =
                mMockBrokenSharedPreferencesHelper.savePersonalInfo(mSharedPreferenceEntry);
        assertThat("Makes sure writing to a broken SharedPreferencesHelper returns false", success,
                is(false));
    }

    private SharedPreferencesHelper createMockSharedPreference() {

        when(mMockSharedPreferences.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString()))
                .thenReturn(mSharedPreferenceEntry.getName());
        when(mMockSharedPreferences.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString())).thenReturn(mSharedPreferenceEntry.getEmail());
        when(mMockSharedPreferences.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong()))
                .thenReturn(mSharedPreferenceEntry.getDateOfBirth().getTimeInMillis());

        when(mMockEditor.commit()).thenReturn(true);

        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
        return new SharedPreferencesHelper(mMockSharedPreferences);
    }



    private SharedPreferencesHelper createBrokenMockSharedPreference() {


        when(mMockBrokenEditor.commit()).thenReturn(false);

        when(mMockBrokenSharedPreferences.edit()).thenReturn(mMockBrokenEditor);
        return new SharedPreferencesHelper(mMockBrokenSharedPreferences);
    }

    }
