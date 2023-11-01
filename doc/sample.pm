<?xml version="1.0" encoding="US-ASCII"?><root timestamp="2022-05-21 15:03:03" version="1.0">
<PortfolioDef id="1000" type="Portfolio">
<Attribute key="id" type="java.lang.String">IT</Attribute>
<Attribute key="department" type="java.lang.String">IT</Attribute>
<ProjectRef id="1001"/>
</PortfolioDef>
<ProjectDef id="1001" type="Project">
<Attribute key="date" type="org.wuerthner.proman.type.SimpleDate">1970-01-01</Attribute>
<Attribute key="description" type="java.lang.String">The "weather" project allows to take weather measurements over a period of time
and creates reports based on the data.</Attribute>
<Attribute key="id" type="java.lang.String">weather</Attribute>
<ComponentRef id="1002"/>
<ComponentRef id="1005"/>
<ComponentRef id="1007"/>
<ReleaseRef id="1010"/>
<ReleaseRef id="1011"/>
<ReleaseRef id="1012"/>
</ProjectDef>
<ComponentDef id="1002" type="Component">
<Attribute key="description" type="java.lang.String">This component provides a user interface to collect the measurement results
and stores them in a database.</Attribute>
<Attribute key="id" type="java.lang.String">Measurement Collection</Attribute>
<ComponentReleaseRef id="1003"/>
<ComponentReleaseRef id="1004"/>
</ComponentDef>
<ComponentReleaseDef id="1003" type="ComponentRelease">
<Attribute key="projectRelease" type="java.util.List">[1.0]</Attribute>
<Attribute key="id" type="java.lang.String">1.0</Attribute>
</ComponentReleaseDef>
<ComponentReleaseDef id="1004" type="ComponentRelease">
<Attribute key="projectRelease" type="java.util.List">[2.0, 2.1]</Attribute>
<Attribute key="id" type="java.lang.String">2.0</Attribute>
</ComponentReleaseDef>
<ComponentDef id="1005" type="Component">
<Attribute key="description" type="java.lang.String">The notification engine allows to configure notifications to be sent to a distribution
list, either event-based or according to a time schedule.</Attribute>
<Attribute key="id" type="java.lang.String">Notification Engine</Attribute>
<ComponentReleaseRef id="1006"/>
</ComponentDef>
<ComponentReleaseDef id="1006" type="ComponentRelease">
<Attribute key="projectRelease" type="java.util.List">[2.0, 2.1]</Attribute>
<Attribute key="id" type="java.lang.String">1.0</Attribute>
</ComponentReleaseDef>
<ComponentDef id="1007" type="Component">
<Attribute key="licenses" type="java.util.List">[apache2, mit]</Attribute>
<Attribute key="thirdParty" type="java.lang.Boolean">true</Attribute>
<Attribute key="description" type="java.lang.String">The report generator creates reports based on all measurements taken in a
specified time interval.</Attribute>
<Attribute key="id" type="java.lang.String">Report Generator</Attribute>
<ComponentReleaseRef id="1008"/>
<ComponentReleaseRef id="1009"/>
</ComponentDef>
<ComponentReleaseDef id="1008" type="ComponentRelease">
<Attribute key="projectRelease" type="java.util.List">[1.0, 2.0]</Attribute>
<Attribute key="id" type="java.lang.String">1.0</Attribute>
</ComponentReleaseDef>
<ComponentReleaseDef id="1009" type="ComponentRelease">
<Attribute key="projectRelease" type="java.util.List">[2.1]</Attribute>
<Attribute key="id" type="java.lang.String">1.1</Attribute>
</ComponentReleaseDef>
<ReleaseDef id="1010" type="Release">
<Attribute key="date" type="org.wuerthner.proman.type.SimpleDate">2022-05-21</Attribute>
<Attribute key="msg" type="java.lang.String">$</Attribute>
<Attribute key="description" type="java.lang.String">The first release with basic functionality</Attribute>
<Attribute key="id" type="java.lang.String">1.0</Attribute>
</ReleaseDef>
<ReleaseDef id="1011" type="Release">
<Attribute key="date" type="org.wuerthner.proman.type.SimpleDate">2020-11-21</Attribute>
<Attribute key="msg" type="java.lang.String">$</Attribute>
<Attribute key="description" type="java.lang.String">Contains a redesing of the measurement collection component
A notification engine is added</Attribute>
<Attribute key="id" type="java.lang.String">2.0</Attribute>
</ReleaseDef>
<ReleaseDef id="1012" type="Release">
<Attribute key="date" type="org.wuerthner.proman.type.SimpleDate">2022-05-21</Attribute>
<Attribute key="msg" type="java.lang.String">$</Attribute>
<Attribute key="description" type="java.lang.String">Fixed bugs no. 4711 and 4712 in report generator</Attribute>
<Attribute key="id" type="java.lang.String">2.1</Attribute>
</ReleaseDef>
</root>
