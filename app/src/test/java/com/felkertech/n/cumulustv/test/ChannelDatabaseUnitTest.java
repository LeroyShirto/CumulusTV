package com.felkertech.n.cumulustv.test;

import android.os.Build;

import com.felkertech.cumulustv.plugins.CumulusChannel;
import com.felkertech.n.cumulustv.BuildConfig;
import com.felkertech.n.cumulustv.MockChannelDatabase;
import com.felkertech.n.cumulustv.model.ChannelDatabase;
import com.felkertech.n.cumulustv.model.JsonChannel;
import com.felkertech.settingsmanager.SettingsManager;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

/**
 * Tests management of channels in the {@link ChannelDatabase}.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class ChannelDatabaseUnitTest extends TestCase {
    private static final String NAME = "My Channel";
    private static final String MEDIA_URL = "http://example.com/stream.m3u8";
    private static final String NUMBER = "1-1";

    /**
     * Deletes data from the {@link SettingsManager}.
     */
    @Before
    public void clearDatabase() {
        MockChannelDatabase.reset();
        SettingsManager settingsManager = new SettingsManager(RuntimeEnvironment.application);
        settingsManager.setString(MockChannelDatabase.KEY, "");
    }

    /**
     * Start out with nothing and try to create a {@link MockChannelDatabase}.
     * Make sure that the various getters do not fail in the null case.
     */
    @Test
    public void testEmptyDatabase() throws JSONException {
        MockChannelDatabase mockChannelDatabase =
                MockChannelDatabase.getMockedInstance(RuntimeEnvironment.application);
        List<JsonChannel> jsonChannelList = mockChannelDatabase.getJsonChannels();
        assertEquals(0, jsonChannelList.size());

        JSONArray jsonArray = mockChannelDatabase.getJSONArray();
        assertEquals(0, jsonArray.length());

        String[] channelNames = mockChannelDatabase.getChannelNames();
        assertEquals(0, channelNames.length);
    }

    /**
     * Tests that we can insert and query channels from the database. Then make sure we can delete
     * it afterward.
     */
    @Test
    public void testChannelInsertion() throws JSONException {
        MockChannelDatabase mockChannelDatabase =
                MockChannelDatabase.getMockedInstance(RuntimeEnvironment.application);
        CumulusChannel sampleChannel = new JsonChannel.Builder()
                .setName(NAME)
                .setNumber(NUMBER)
                .setMediaUrl(MEDIA_URL)
                .build();
        mockChannelDatabase.add(sampleChannel);

        List<JsonChannel> jsonChannelList = mockChannelDatabase.getJsonChannels();
        assertEquals(sampleChannel, jsonChannelList.get(0));

        JSONArray jsonArray = mockChannelDatabase.getJSONArray();
        assertEquals(sampleChannel.toJSON().toString(), jsonArray.getJSONObject(0).toString());

        String[] channelNames = mockChannelDatabase.getChannelNames();
        assertEquals(sampleChannel.getNumber() + " " + sampleChannel.getName(), channelNames[0]);

        mockChannelDatabase.delete(sampleChannel);
        jsonChannelList = mockChannelDatabase.getJsonChannels();
        assertEquals(0, jsonChannelList.size());
    }

    /**
     * Tests that we can insert and then check whether this channel exists and finally find it.
     */
    @Test
    public void testChannelFind() throws JSONException {
        MockChannelDatabase mockChannelDatabase =
                MockChannelDatabase.getMockedInstance(RuntimeEnvironment.application);
        CumulusChannel sampleChannel = new JsonChannel.Builder()
                .setName(NAME)
                .setNumber(NUMBER)
                .setMediaUrl(MEDIA_URL)
                .build();
        mockChannelDatabase.add(sampleChannel);

        assertTrue(mockChannelDatabase.channelExists(sampleChannel));
        assertTrue(mockChannelDatabase.channelNumberExists(NUMBER));
        assertEquals(sampleChannel, mockChannelDatabase.findChannelByMediaUrl(MEDIA_URL));
    }
}
