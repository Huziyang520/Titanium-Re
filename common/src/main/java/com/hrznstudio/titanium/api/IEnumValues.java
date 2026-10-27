// Titanium Lite I Enum Values

package com.hrznstudio.titanium.api;

import java.util.List;

public interface IEnumValues<T> {

    List<T> getValues();

    T getValue(String name);

}
