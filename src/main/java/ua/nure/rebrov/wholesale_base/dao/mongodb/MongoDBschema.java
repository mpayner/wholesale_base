package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;

public class MongoDBschema {
    protected MongoDatabase con;

    public MongoDBschema(){
        try {
            con = MongoDBConnector.getDefaultConnection();
        }catch (MongoException e){
            e.printStackTrace();
        }
    }
}
