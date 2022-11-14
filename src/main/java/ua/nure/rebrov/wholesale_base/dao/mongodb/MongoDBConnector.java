package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnector {
    private static final String dbUser = "root";
    private static final String dbPassword = "1234";
    private static final String dbName = "wholesale_base";
    private static String dbUrl = "mongodb://localhost:27017/";
    private static MongoDatabase con = null;

    public static MongoDatabase getDefaultConnection() {
        if (con == null)
        {
            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().conventions(Arrays.asList(Conventions.ANNOTATION_CONVENTION)).automatic(true).build());
            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
            MongoClient mongoClient = MongoClients.create(dbUrl);
            return con = mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry);
        }
        return con;
    }
}