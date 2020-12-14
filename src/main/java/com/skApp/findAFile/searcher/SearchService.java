package com.skApp.findAFile.searcher;

import com.skApp.findAFile.shared.mapper.response.SearchOutput;
import com.skApp.findAFile.shared.persistance.entity.DocumentDetail;
import com.skApp.findAFile.shared.persistance.repo.DocumentDetailRepo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    public final int MAX_SEARCH = 200;
    private final String FILE_NAME = "filename";
    private final DocumentDetailRepo documentDetailRepo;
    private QueryParser queryParser;
    private ApplicationContext context;
    private IndexSearcher indexSearcher;

    @Autowired
    public SearchService(QueryParser queryParser, ApplicationContext context, DocumentDetailRepo documentDetailRepo) {
        this.queryParser = queryParser;
        this.context = context;
        this.documentDetailRepo = documentDetailRepo;
    }

    public List<SearchOutput> search(String searchQuery) throws IOException, ParseException {

        log.info("Inside search with search string :" + searchQuery);
        queryParser.setAllowLeadingWildcard(true);
        queryParser.setPhraseSlop(300);
        Query query = queryParser.parse(searchQuery);
        indexSearcher = getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, MAX_SEARCH);
        return Arrays.stream(topDocs.scoreDocs).map(this::getDocument).collect(Collectors.toList());
    }

    @SneakyThrows
    public SearchOutput getDocument(ScoreDoc scoreDoc) {

        String docName = indexSearcher.doc(scoreDoc.doc).get(FILE_NAME);
        DocumentDetail documentDetail = documentDetailRepo.findByDocName(docName);
        if (null != documentDetail) {
            String userName = (null != documentDetail.getUploadUser()) ? documentDetail.getUploadUser().getUserName() : null;
            return new SearchOutput(docName, userName, documentDetail.getDescription(), null);
        } else return null;
    }

    private IndexSearcher getIndexSearcher() {
        return (IndexSearcher) context.getBean("indexSearcher");
    }

}
