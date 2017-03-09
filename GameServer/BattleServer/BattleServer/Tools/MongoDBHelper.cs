using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MongoDB.Bson;
using MongoDB.Driver;
using MongoDB.Driver.Builders;

namespace RedStone
{
	internal static class DB
	{
		private static MongoDatabase db { get { return DBManager.instance.GetDB("xiao_game"); } }
		public static MongoCollection user { get { return db.GetCollection<BsonDocument>("user"); } }
		public static MongoCollection user_log { get { return db.GetCollection<BsonDocument>("user_log"); } }
		public static MongoCollection battle_log { get { return db.GetCollection<BsonDocument>("battle_log"); } }

		public static class User
		{
			public static MongoCollection coll { get { return user; } }

			public static bool IsExist(string deviceUID)
			{
				var u = coll.FindOneAs<BsonDocument>(Query.EQ("device_uid", deviceUID));
				return u != null;
			}

			public static BsonDocument Login(string deviceUID)
			{
				if (!IsExist(deviceUID))
				{
					Create(deviceUID);
				}
				var q = Query.EQ("device_uid", deviceUID);
				coll.Update(q, Update.Set("online", true));
				return coll.FindOneAs<BsonDocument>(q);
			}

			public static void Logout(long uid)
			{
				coll.Update(Query.EQ("uid", uid), Update.Set("online", false));
			}

			public static void Create(string deviceUID)
			{
				var doc = new BsonDocument();
				long uid = GUID.UserID;
				doc.Add("device_uid", deviceUID);
				doc.Add("uid", uid);
				doc.Add("online", false);
				doc.Add("level", 1);
				doc.Add("exp", 0);
				doc.Add("name", "P_" + uid);
				coll.Insert(doc);
			}
		}
	}
}
