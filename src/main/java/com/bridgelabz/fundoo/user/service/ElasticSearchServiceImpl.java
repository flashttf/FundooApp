package com.bridgelabz.fundoo.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearchServiceImpl implements IElasticSearchService {

	String INDEX = "es";
	String TYPE = "noteCreate";

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	Environment environment;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String createNote(Note note) throws IOException {

		@SuppressWarnings({ "unchecked" })
		Map<String, Object> noteMapper = objectMapper.convertValue(note, Map.class);

		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, note.getNoteId()).source(noteMapper);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

		return indexResponse.getResult().name();

	}

	@Override
	public String updateNote(Note note) throws Exception {
		Note noteDocument = findById(note.getNoteId());

		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, noteDocument.getNoteId());

		@SuppressWarnings("unchecked")
		Map<String, Object> noteMapper = objectMapper.convertValue(note, Map.class);

		updateRequest.doc(noteMapper);

		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
		return updateResponse.getResult().name();
//		Response response=ResponseUtility.getResponse(202, "",environment.getProperty("elastic.noteUpdate.success"));
//		return response;
	}

	@Override
	public String deleteNote(String id) throws Exception {

		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
		return deleteResponse.getResult().name();
	}

	@Override
	public Note findById(String noteId) throws Exception {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, noteId);

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		Map<String, Object> resultMap = getResponse.getSource();
		return objectMapper.convertValue(resultMap, Note.class);
	}

	@Override
	public List<Note> findByTitle(String title, String userId) throws Exception {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", title))
				.filter(QueryBuilders.termsQuery("userId", userId));

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder);

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		return getSearchResult(searchResponse);

	}

	private List<Note> getSearchResult(SearchResponse searchResponse) throws Exception {
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Note> noteDocuments = new ArrayList<>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
					.forEach(hit -> noteDocuments.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
		}
		return noteDocuments;
	}

	public List<Note> findAll() throws Exception {
		SearchRequest searchRequest = new SearchRequest();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		return getSearchResult(searchResponse);
	}
}
