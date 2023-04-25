package id.co.cryptocore.cryptocore.model.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import id.co.cryptocore.cryptocore.model.Currency;

import java.io.IOException;

public class CurrencySymbolSerializer extends JsonSerializer<Currency> {
    @Override
    public void serialize(Currency currency, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(currency.getSymbol());
    }
}
