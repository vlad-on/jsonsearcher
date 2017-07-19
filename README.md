# JsonSearcher
<p>Web page which allow to search programming languages from Json file called data.json.</p>
<h3>How to start:</h3>
<p>Clone the source code from github, update all needed maven dependencies and start main method from JsonSearcherApplication class.</p>
<p>As this code uses Spring Bootstrap it starts Tomcat 8 on home page http://localhost:8080/ out of the box. It also can be modified to get jar/war file to upload it on a remote server.</p>
<h3>Main functionality:</h3>
<ol>
  <li>Main page contains a search form with dropdowns to change number of results per page and result sorting type</li>
  <li>To start searching just start typing in the search input, for example 'Java'.</li>
  <li>Main syntax is type all words that you need to find separating them with spaces, for example you can find all languages with author 'James Gosling'</li>
  <li>The order of words is irrelevant, however the algorithm first tries to find exact match and only then changes the word order</li>
  <li>To exclude some words add '-' in the beggining of such words also separating them with spaces. For example 'Java -JavaScript Gosling James -Script' should return Java language</li>
  <li>The search process fires whenever you change search input or click button 'Search' next to the input - it does the same.</li>
  <li>Search is non-case sensitive</li>
</ol>
<h3>Class hierarchy:</h3>
<p>There are model classes which contain ProgrammingLanguage class, it's comparator PrLangByNameComparer and web UI+model wrapper called UIModel.</p>
<p>There is a repository class MainRepository which makes all needed transorms to get a Set of Programming Languages from data.json file.</p>
<p>In the MainController class is all needed mapping logic for calling search page and ajax queries.</p>
<p>The last two of them are service classes. MainService contains all needed logic to convert and get all needed data that was called by MainController. The SearchEngineService class contains all logic related to search process itself.</p>
<p>In a resources package there are UI files (templates, css, js, fonts), data.json and application.properties</p>
<p>There are also several tests almost for each method in the application.</p>
<h3>Used techonologies:</h3>
<ul>
    <li>Java 8</li>
    <li>JavaScript</li>
    <li>Google gson, guava</li>
    <li>Spring Framework 4
        <ul>
            <li>Spring Boot</li>
            <li>Spring Core</li>
            <li>Spring MVC</li>
            <li>Spring Web</li>
            <li>Spring Web Services</li>
        </ul>
    </li>
    <li> UI
        <ul>
            <li>Thymeleaf 2</li>
            <li>JQuery 2</li>
            <li>Bootstrap 3</li>
            <li>CSS3</li>
        </ul>
    </li>
    <li>Testing
        <ul>
            <li>JUnit 4.1</li>
            <li>Mockito</li>
            <li>PowerMock</li>
        </ul>
    </li>
</ul>
