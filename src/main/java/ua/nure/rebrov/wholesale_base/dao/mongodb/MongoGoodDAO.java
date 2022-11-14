package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import ua.nure.rebrov.wholesale_base.dao.GoodDAO;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoDBConnector;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.User;

import java.util.LinkedList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoGoodDAO implements GoodDAO {

    private static MongoDatabase con = null;
    public MongoGoodDAO()  {
        con = MongoDBConnector.getDefaultConnection();
    }

    public List<Good> getAll(){
        List<Good> l  = new LinkedList<>();
        MongoCollection<Good> note = con.getCollection("ad", Good.class);
        FindIterable<Good> Goods = note.find();
        MongoCursor<Good> cursor = Goods.cursor();
        while(cursor.hasNext()){

            l.add(cursor.next());
        }
        return l;
    }

    @Override
    public List<Good> getPriceList(User distributor) {
        return null;
    }

    @Override
    public List<Good> getBaseGoods() {
        return null;
    }

    public Good getById(String id){
        MongoCollection<Good> collection = con.getCollection("ad", Good.class);
        MongoCursor<Good> cursor = collection.find(new Document("_id", new ObjectId(id))).cursor();
        return !cursor.hasNext() ? null : cursor.next();
    }

    @Override
    public boolean updateQuantity(Good good, Integer quantity) {
        return false;
    }

    @Override
    public boolean increaseQuantity(Good good, Integer quantity) {
        return false;
    }

    @Override
    public boolean decreaseQuantity(Good good, Integer quantity) {
        return false;
    }

    @Override
    public Integer getQuantity(Good good) {
        return 0;
    }

    public boolean deleteById(String id){
        DeleteResult res = con.getCollection("ad").deleteOne(new Document("_id", new ObjectId(id)));

        return res.getDeletedCount()!=0;
    }

    public void add(Good Good){

        MongoCollection<Good> nt = con.getCollection("user", Good.class);

        nt.insertOne(Good);
    }

    @Override
    public void update(Good good) {

    }
}
