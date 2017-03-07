using System;
using System.Collections.Generic;
using RedStone.Net;
using MongoDB.Bson;
using MongoDB.Driver;
using MongoDB.Driver.Builders;

namespace RedStone
{
    public class DBManager : Core.Singleton<DBManager>
    {
        private MongoClient client = new MongoClient("mongodb://localhost:27017");
        private Dictionary<string, MongoDatabase> dbs = new Dictionary<string, MongoDatabase>();
        public void Init()
        {
            AddDataBase("xiao_game");
        }

        public void AddDataBase(string name)
        {
            var database = client.GetServer().GetDatabase(name);
            dbs.Add(name, database);
        }

        public MongoDatabase GetDB(string name)
        {
            return dbs[name];
        }




        private void Test()
        {
            var database = GetDB("xiao_game");
            var collection = database.GetCollection<BsonDocument>("bar");
            collection.Insert(new BsonDocument("Name", "Jack"));
            IMongoQuery query = Query.GTE("Name", "Jack");
            var entity = collection.FindOne(query);
        }

    }
}

