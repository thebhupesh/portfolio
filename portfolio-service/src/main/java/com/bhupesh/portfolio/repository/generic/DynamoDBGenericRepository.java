package com.bhupesh.portfolio.repository.generic;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

public class DynamoDBGenericRepository<T> {

    private final DynamoDbTable<T> table;
    private final String type;

    public DynamoDBGenericRepository(DynamoDbEnhancedClient client, String tableName, String type, Class<T> beanClass) {

        this.table = client.table(
                tableName,
                TableSchema.fromBean(beanClass));

        this.type = type;
    }

    public List<T> findAll() {

        QueryConditional condition = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(type)
                        .build());

        return table.query(condition)
                .items()
                .stream()
                .toList();
    }

    public T findById(String id) {

        Key key = Key.builder()
                .partitionValue(type)
                .sortValue(id)
                .build();

        return table.getItem(key);
    }

    public void save(T item) {
        table.putItem(item);
    }

    public void update(T item) {
        table.updateItem(item);
    }

    public void deleteById(String id) {
        Key key = Key.builder()
                .partitionValue(type)
                .sortValue(id)
                .build();

        table.deleteItem(key);
    }
}
