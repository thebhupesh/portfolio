package com.bhupesh.portfolio.repository.generic;

import java.util.List;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
public class DynamoDBRepository<T> {

    private final DynamoDbTable<T> table;
    private final String type;

    public DynamoDBRepository(DynamoDbEnhancedClient client, String tableName,String type, Class<T> beanClass) {

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
}
