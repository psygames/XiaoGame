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
        public void Init()
        {
            var client = new MongoClient("mongodb://localhost:27017");
            var database = client.GetServer().GetDatabase("foo");
            var collection = database.GetCollection<BsonDocument>("bar");

            collection.Insert(new BsonDocument("Name", "Jack"));

            IMongoQuery query = Query.GTE("Name", "Jack");
            var entity = collection.FindOne(query);
        }
    }
}

