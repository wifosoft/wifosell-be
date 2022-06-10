package com.wifosell.zeus.config.lucene;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class LuceneAnalysisConfig implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("vieNGram").custom()
                .tokenizer(StandardTokenizerFactory.NAME)
                .tokenFilter(LowerCaseFilterFactory.NAME)
                .tokenFilter(ASCIIFoldingFilterFactory.NAME)
                .param("preserveOriginal", "true")
                .tokenFilter(EdgeNGramFilterFactory.NAME)
                .param("minGramSize", "1")
                .param("maxGramSize", "20")
                .param("preserveOriginal", "true");

        context.analyzer("engNGram").custom()
                .tokenizer(StandardTokenizerFactory.NAME)
                .tokenFilter(LowerCaseFilterFactory.NAME)
                .tokenFilter(EdgeNGramFilterFactory.NAME)
                .param("minGramSize", "1")
                .param("maxGramSize", "20")
                .param("preserveOriginal", "true");
    }
}
