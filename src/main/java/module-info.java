module tech.sirwellington.alchemy.test {
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.mockito;
    requires org.hamcrest;
    requires org.slf4j;
    requires tech.sirwellington.alchemy.annotations;
    requires tech.sirwellington.alchemy.generator;
    requires org.mockito.junit.jupiter;

    exports tech.sirwellington.alchemy.test.junit;
    exports tech.sirwellington.alchemy.test.junit.generation;
    exports tech.sirwellington.alchemy.test.mockito;
}