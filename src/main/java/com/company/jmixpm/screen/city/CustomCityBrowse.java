package com.company.jmixpm.screen.city;

import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.City;

@UiController("city-list")
@UiDescriptor("custom-city-browse.xml")
@LookupComponent("citiesTable")
public class CustomCityBrowse extends StandardLookup<City> {
}