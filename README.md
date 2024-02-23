# GPS-API

An API built with Spring Boot and PostgreSQL, meant to simulate the functionality of a GPS.

## Description

Location data (points on a 2D plane with Name & ID fields) are stored in a PostgreSQL table. Through CRUD operations, the API allows users to create/update/delete location data, 
as well as perform a variety of searches (get locations by ID, search locations within a circle range, find closest location to a point). A quadtree data structure has been 
implemented for its ability to organize spatial data. This significantly reduces the runtime of these search functions. As proof, most search functions have a "slow" 
counterpart (getLocsWithinCircle vs. getLocsWithinCircleSlow) in which searches are made simply by iterating through the repository's records.

<br>
<p align="center">
  <img src="https://github.com/pdbhenry/GPS-API/assets/36090515/c16f567d-7fec-4a4e-bc29-9357c3d983a0" width="375"/>
  <img src="https://github.com/pdbhenry/GPS-API/assets/36090515/6fb965eb-1fe0-491e-8d96-460cc0f77aa9" width="360"/>
</p>

<h6 align="center">
  Left: Basic visualization of a quadtree—locations are red dots, which are categorized into quadrants, or nodes.
  <br>
  Right: A comparison of runtimes when searching for a location by iterating through a repository vs. searching the quadtree. 
  <br>
  Tested with 2000 location records present.
</h6>
  
<br>

### Executing program
Ran in IntelliJ IDEA. API tested with Postman.
