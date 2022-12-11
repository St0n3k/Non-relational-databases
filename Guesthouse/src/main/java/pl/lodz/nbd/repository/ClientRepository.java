package pl.lodz.nbd.repository;

public class ClientRepository {


//    public ClientRepository() {
//        clientCollection.drop();
//        clientCollection = mongoDatabase.getCollection("clients", Client.class);
//        Document index = new Document("personal_id", 1);
//        IndexOptions options = new IndexOptions().unique(true);
//        clientCollection.createIndex(index, options);
//    }
//
//
//    public Optional<Client> add(Client client) {
//        Optional<Client> optionalClient;
//        try {
//            if (clientCollection.insertOne(client).wasAcknowledged()) {
//                optionalClient = Optional.of(client);
//            } else {
//                optionalClient = Optional.empty();
//            }
//            return optionalClient;
//        } catch (MongoWriteException e) {
//            return Optional.empty();
//        }
//    }
//
//    public List<Client> getAll() {
//        return clientCollection.find().into(new ArrayList<>());
//    }
//
//
//    public void remove(Client client) {
//        Bson filter = Filters.eq("_id", client.getUuid());
//        clientCollection.findOneAndDelete(filter);
//    }
//
//
//    public Optional<Client> getById(UUID id) {
//        Bson filter = Filters.eq("_id", id);
//        return Optional.ofNullable(mongoDatabase.getCollection("clients", Client.class).find(filter).first());
//    }
//
//
//    public Optional<Client> getClientByPersonalId(String personalId) {
//        Bson filter = Filters.eq("personal_id", personalId);
//        return Optional.ofNullable(mongoDatabase.getCollection("clients", Client.class).find(filter).first());
//    }
//
//    public boolean update(Client client) {
//        Bson filter = Filters.eq("_id", client.getUuid());
//        Bson update = Updates.combine(
//                Updates.set("first_name", client.getFirstName()),
//                Updates.set("last_name", client.getLastName()),
//                Updates.set("clientType", client.getClientType()),
//                Updates.set("address", client.getAddress())
//
//        );
//        return mongoDatabase.getCollection("clients", Client.class).updateOne(filter, update).wasAcknowledged();
//    }

}
