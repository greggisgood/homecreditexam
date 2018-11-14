package com.homecredit.exam.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.homecredit.exam.constants.Values;
import com.homecredit.exam.models.City;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CacheHandler {
    private CacheManager manager;

    public static CacheHandler getInstance(Context context)
    {
        return new CacheHandler(context);
    }

    public static CacheHandler getInstance(CacheManager manager)
    {
        return new CacheHandler(manager);
    }

    private CacheHandler(Context context)
    {
        manager = CacheManager.with(context);
    }

    private CacheHandler(CacheManager manager)
    {
        this.manager = manager;
    }

    /**
     * Retrieves the list of Cities from the Cache
     * @return - A list of Cities
     */
    public List<City> getCities()
    {
        List<City> cities = new ArrayList<>();
        try
        {
            String data = manager.readFromFile(Values.CITIES_LIST);
            if (StringUtils.isNotBlank(data))
            {
                Gson gson = new Gson();
                Type type = new TypeToken<List<City>>(){}.getType();
                cities = gson.fromJson(data, type);
            }
        }
        catch (NullPointerException | JsonParseException e)
        {
            e.printStackTrace();
        }
        return cities;
    }
}
