package fr.uge.tp3.avro;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import fr.uge.tp3.models.Prescription;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class AvroSeDes<T> {
    private final Schema schema;
    private final Injection<GenericRecord, byte[]> recordInjection;

    private AvroSeDes(Schema schema, Injection<GenericRecord, byte[]> recordInjection) {
        Objects.requireNonNull(this.schema = schema);
        Objects.requireNonNull(this.recordInjection = recordInjection);
    }

    public byte[] serialize(T object) throws IllegalAccessException {
        Objects.requireNonNull(object);
        GenericData.Record avroRecord = new GenericData.Record(schema);

        for(var field: object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            avroRecord.put(field.getName(), field.get(object));
        }

        return recordInjection.apply(avroRecord);
    }

    public T deserialize(byte[] data, T object) {
        Objects.requireNonNull(data);
        var genericRecord = recordInjection.invert(data).get();

        // ¯\_(ツ)_/¯
        try {
            for (var field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                var value = genericRecord.get(field.getName());

                if (value.getClass() == org.apache.avro.util.Utf8.class)
                    field.set(object, genericRecord.get(field.getName()).toString());
                else
                    field.set(object, genericRecord.get(field.getName()));
            }
        } catch(Exception e) {
            System.out.println("Error" + e);
        }

        return object;
    }

    static public <T> AvroSeDes<T> build(Path path) throws IOException {
        var parser = new Schema.Parser();
        var schema = parser.parse(path.toFile());
        var recordInjection = GenericAvroCodecs.toBinary(schema);

        return new AvroSeDes<T>(schema, recordInjection);
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        Path schemaPath = Path.of("src/main/resources/Prescription.avsc");

        AvroSeDes<Prescription> t = AvroSeDes.build(schemaPath);
        var p = new Prescription("test", "en", 1, 2, 3);
        var bytes = t.serialize(p);
        var p2 = new Prescription();
        t.deserialize(bytes, p2);
        System.out.println(p2);
    }
}
