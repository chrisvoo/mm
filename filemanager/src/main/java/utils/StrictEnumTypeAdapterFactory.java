package utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import exceptions.ModelException;

import java.io.IOException;
import java.io.StringReader;

public class StrictEnumTypeAdapterFactory implements TypeAdapterFactory {
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<T> rawType = (Class<T>) type.getRawType();
    if (!rawType.isEnum()) {
      return null;
    }
    return newStrictEnumAdapter(gson.getDelegateAdapter(this, type));
  }

  private <T> TypeAdapter<T> newStrictEnumAdapter(
    final TypeAdapter<T> delegateAdapter) {
    return new TypeAdapter<>() {

      @Override
      public void write(JsonWriter out, T value) throws IOException {
        delegateAdapter.write(out, value);
      }

      @Override
      public T read(JsonReader in) throws IOException {
        // Peek at the next value and save it for the error message
        // if you don't need the offending value's actual name
        String enumValue = in.nextString();
        JsonReader delegateReader = new JsonReader(new StringReader('"' + enumValue + '"'));
        T value = delegateAdapter.read(delegateReader);
        delegateReader.close();

        if (value == null) {
          throw new ModelException(
            "Invalid enum value - " + enumValue,
            ModelException.INVALID_FIELDS
          );
        }

        return value;
      }
    };
  }
}