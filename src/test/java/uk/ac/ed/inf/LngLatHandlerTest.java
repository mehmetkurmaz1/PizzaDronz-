package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import static uk.ac.ed.inf.ilp.constant.SystemConstants.CENTRAL_REGION_NAME;

public class LngLatHandlerTest
        extends TestCase {
    public LngLatHandlerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LngLatHandlerTest.class);
    }


    /**
     * Test Case: Verifying Point Inside Triangle
     * This method tests whether a given point (3.0, 1.0) lies within a 4x4x4 triangle.
     * The expected result is true, indicating that the point is inside the triangle.
     */
    public void testPointWithinTriangle() {
        // Define the vertices of the triangle
        LngLat[] triangleVertices = {
                new LngLat(0.0, 0.0), // Vertex 1
                new LngLat(4.0, 0.0), // Vertex 2
                new LngLat(0.0, 4.0)  // Vertex 3
        };

        // Create the triangle region
        NamedRegion triangleRegion = new NamedRegion("test area", triangleVertices);

        // Instantiate the handler for LngLat points
        LngLatHandler triangleHandler = new LngLatHandler();

        // Point to be tested
        LngLat testPoint = new LngLat(3.0, 1.0);

        // Check if the test point is within the triangle
        boolean isInsideTriangle = triangleHandler.isInRegion(testPoint, triangleRegion);

        // Assert that the point is inside the triangle
        assertTrue(isInsideTriangle);
    }


    /**
     * Test Case: Verifying Point Inside Square
     * This method tests whether a given point (1.0, 3.0) is inside a 10x10 square area.
     * The expected result is true, confirming the point's presence within the square.
     */
    public void testPointWithinSquare() {
        // Initialize the vertices of the square
        LngLat[] squareVertices = {
                new LngLat(0.0, 10.0), // Vertex 1
                new LngLat(0.0, 0.0),  // Vertex 2
                new LngLat(10.0, 0.0), // Vertex 3
                new LngLat(10.0, 10.0) // Vertex 4
        };

        // Create the square region
        NamedRegion squareRegion = new NamedRegion("test area", squareVertices);

        // Instantiate the LngLatHandler
        LngLatHandler squareHandler = new LngLatHandler();

        // Define the point to be tested
        LngLat testPoint = new LngLat(1.0, 3.0);

        // Check if the test point is within the square
        boolean isPointInsideSquare = squareHandler.isInRegion(testPoint, squareRegion);

        // Assert that the point is inside the square
        assertTrue(isPointInsideSquare);
    }

    /**
     * Test Case: Point on Pentagon Vertex
     * This method tests if a point (6.0, 5.0), located on a vertex of a pentagon, is considered inside the pentagon.
     * The expected result is true, indicating that the point is recognized as being within the pentagon.
     */
    public void testPointOnPentagonVertex() {
        // Define the vertices of the pentagon
        LngLat[] pentagonVertices = {
                new LngLat(0.0, 8.0),  // Vertex 1
                new LngLat(6.0, 5.0),  // Vertex 2
                new LngLat(6.0, -1.0), // Vertex 3
                new LngLat(-2.0, -4.0), // Vertex 4
                new LngLat(-5.0, 3.0)  // Vertex 5
        };

        // Create the pentagon region
        NamedRegion pentagonRegion = new NamedRegion("test area", pentagonVertices);

        // Instantiate the handler for LngLat points
        LngLatHandler pentagonHandler = new LngLatHandler();

        // Define the point to be tested
        LngLat testPoint = new LngLat(6.0, 5.0);

        // Check if the test point is within the pentagon
        boolean isInsidePentagon = pentagonHandler.isInRegion(testPoint, pentagonRegion);

        // Assert that the point is inside the pentagon
        assertTrue(isInsidePentagon);
    }


    /**
     * Test Case: Point on Square Edge
     * This method tests if a point (0.0, 5.0), located on the edge of a 10x10 square, is considered inside the square.
     * The expected outcome is true, suggesting that the point on the edge is part of the square.
     */
    public void testPointOnSquareEdge() {
        // Define the vertices of the square
        LngLat[] squareVertices = {
                new LngLat(0.0, 10.0), // Vertex 1
                new LngLat(0.0, 0.0),  // Vertex 2
                new LngLat(10.0, 0.0), // Vertex 3
                new LngLat(10.0, 10.0) // Vertex 4
        };

        // Create the square region
        NamedRegion squareRegion = new NamedRegion("test area", squareVertices);

        // Instantiate the handler for LngLat points
        LngLatHandler squareHandler = new LngLatHandler();

        // Define the point to be tested (on the edge of the square)
        LngLat testPointOnEdge = new LngLat(0.0, 5.0);

        // Check if the test point is within the square
        boolean isPointInsideSquare = squareHandler.isInRegion(testPointOnEdge, squareRegion);

        // Assert that the point is inside the square
        assertTrue(isPointInsideSquare);
    }


    /**
     * Test Case: Point on Square Vertex
     * This method checks if a point (0.0, 0.0), coinciding with a square's vertex, is considered inside the square.
     * The expected result is true, indicating inclusion within the square's boundaries.
     */
    public void testPointOnSquareVertex() {
        // Define the vertices of the square directly in an array
        LngLat[] squareVertices = {
                new LngLat(0.0, 10.0), // Vertex 1
                new LngLat(0.0, 0.0),  // Vertex 2
                new LngLat(10.0, 0.0), // Vertex 3
                new LngLat(10.0, 10.0) // Vertex 4
        };

        // Instantiate the handler for LngLat points
        LngLatHandler handler = new LngLatHandler();

        // Create the square region with the defined vertices
        NamedRegion region = new NamedRegion("test area", squareVertices);

        // Define the point to be tested (coinciding with a vertex of the square)
        LngLat p = new LngLat(0.0, 0.0);

        // Check if the test point is within the square
        boolean output = handler.isInRegion(p, region);

        // Assert that the point is inside the square
        assertTrue(output);
    }


    /**
     * Test Case: Point Outside Square (Lower Bounds)
     * This test verifies if a point (-1.0, -3.0), located outside a 10x10 square area, is correctly identified as outside.
     * The expected result is false, confirming the point is not within the square.
     */
    public void testPointOutsideSquareLowerBounds() {
        // Initialize vertices of the region
        LngLat[] vertices = {
                new LngLat(0.0, 10.0), // v1
                new LngLat(0.0, 0.0),  // v2
                new LngLat(10.0, 0.0), // v3
                new LngLat(10.0, 10.0) // v4
        };

        // Create region and handler objects
        NamedRegion testRegion = new NamedRegion("test area", vertices);
        LngLatHandler lngLatHandler = new LngLatHandler();

        // Point to test
        LngLat testPoint = new LngLat(-1.0, -3.0);

        // Check if the point is inside the region
        boolean isInside = lngLatHandler.isInRegion(testPoint, testRegion);

        // Assert that the point is not inside the region
        assertFalse(isInside);
    }


    /**
     * Test Case: Point Outside Square (Upper Bounds)
     * This test assesses if a point (12.0, 11.0), outside the bounds of a 10x10 square, is identified as not being inside the square.
     * The expected outcome is false, affirming the point's location outside the square.
     */
    public void testPointOutsideSquareUpperBounds() {
        // Define the region with its vertices
        LngLat[] regionVertices = {
                new LngLat(0.0, 10.0), // Vertex 1
                new LngLat(0.0, 0.0),  // Vertex 2
                new LngLat(10.0, 0.0), // Vertex 3
                new LngLat(10.0, 10.0) // Vertex 4
        };

        // Create the region object
        NamedRegion squareRegion = new NamedRegion("test area", regionVertices);

        // Instantiate the handler for longitude and latitude points
        LngLatHandler squareHandler = new LngLatHandler();

        // Point to be tested
        LngLat pointToTest = new LngLat(12.0, 11.0);

        // Check if the point is within the defined region
        boolean isInSquare = squareHandler.isInRegion(pointToTest, squareRegion);

        // Assert that the point is not inside the region
        assertFalse(isInSquare);
    }


    /**
     * Test Case: Point Outside Pentagon
     * This test evaluates if a point (7.0, 7.0), situated outside a pentagon, is correctly identified as not being inside it.
     * The expected result is false, indicating the point does not lie within the pentagon.
     */
    public void testPointOutsidePentagon() {
        // Define the pentagon's vertices
        LngLat[] pentagonVertices = {
                new LngLat(0.0, 8.0),  // Vertex 1
                new LngLat(6.0, 5.0),  // Vertex 2
                new LngLat(6.0, -1.0), // Vertex 3
                new LngLat(-2.0, -4.0), // Vertex 4
                new LngLat(-5.0, 3.0)  // Vertex 5
        };

        // Create the pentagon region
        NamedRegion pentagonRegion = new NamedRegion("test area", pentagonVertices);

        // Instantiate the handler for LngLat points
        LngLatHandler pentagonHandler = new LngLatHandler();

        // Point to be tested
        LngLat testPoint = new LngLat(6.0, 5.0);

        // Check if the test point is within the pentagon
        boolean isPointInside = pentagonHandler.isInRegion(testPoint, pentagonRegion);

        // Assert that the point is inside the pentagon
        assertTrue(isPointInside);
    }

    /**
     * Test Case: Hover Position Validation
     * This test checks if a hover operation at an arbitrary angle (999) from position (0.0, 0.0) maintains the same position.
     * The expected result is true, indicating no change in position after the hover operation.
     */
    public void testHover() {
        // Define starting and expected positions
        LngLat startPosition = new LngLat(0.0, 0.0);
        LngLat expectedPosition = new LngLat(0.0, 0.0);

        // Define an arbitrary angle
        double arbitraryAngle = 999;

        // Instantiate the LngLatHandler
        LngLatHandler lngLatHandler = new LngLatHandler();

        // Calculate the next position based on the starting position and angle
        LngLat calculatedNextPosition = lngLatHandler.nextPosition(startPosition, arbitraryAngle);

        // Assert that the calculated position is close to the expected position
        assertTrue(lngLatHandler.isCloseTo(calculatedNextPosition, expectedPosition));
    }


    // Tests the movement of the point towards East
    public void testEast() {
        performDirectionTest(new LngLat(0.0, 0.0), new LngLat(0.00015, 0.0), 0.0);
    }

    // Tests the movement of the point towards West
    public void testWest() {
        performDirectionTest(new LngLat(0.0, 0.0), new LngLat(-0.00015, 0.0), 180.0);
    }

    // Tests the movement of the point towards North
    public void testNorth() {
        performDirectionTest(new LngLat(0.0, 0.0), new LngLat(0.0, 0.00015), 90.0);
    }

    // Tests the movement of the point towards South
    public void testSouth() {
        performDirectionTest(new LngLat(0.0, 0.0), new LngLat(0.0, -0.00015), 270.0);
    }

    // Tests the movement of the point towards North East
    public void testNorthEast() {
        performDirectionTest(new LngLat(1.0, 1.0), new LngLat(1.000106066, 1.000106066), 45.0);
    }

    // Tests the movement of the point towards North West
    public void testNorthWest() {
        performDirectionTest(new LngLat(1.0, 1.0), new LngLat(0.999893934, 1.000106066), 135.0);
    }

    // Tests the movement of the point towards South West
    public void testSouthWest() {
        performDirectionTest(new LngLat(1.0, 1.0), new LngLat(0.999893934, 0.999893934), 225.0);
    }

    // Tests the movement of the point towards South East
    public void testSouthEast() {
        performDirectionTest(new LngLat(1.0, 1.0), new LngLat(1.000106066, 0.999893934), 315.0);
    }

    // Helper method to perform the direction test
    private void performDirectionTest(LngLat startingPosition, LngLat expectedNextPosition, double angle) {
        LngLatHandler handler = new LngLatHandler();
        LngLat output = handler.nextPosition(startingPosition, angle);
        assertTrue(handler.isCloseTo(output, expectedNextPosition));
    }

    public void testEastNorthEast() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-2.999861418, 56.0000574), 22.5, true);
    }

    // Tests the movement of the point towards North North East
    public void testNorthNorthEast() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-2.999942597, 56.00013858), 67.5, true);
    }

    // Tests the movement of the point towards North North West
    public void testNorthNorthWest() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-3.000057403, 56.00013858), 112.5, true);
    }

    // Tests the movement of the point towards West North West
    public void testWestNorthWest() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-3.000138582, 56.0000574), 157.5, true);
    }

    // Tests the movement of the point towards West South West
    public void testWestSouthWest() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-3.000138582, 55.9999426), 202.5, true);
    }

    // Tests the movement of the point towards South South West
    public void testSouthSouthWest() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-3.000057403, 56.1), 247.5, false);
    }

    // Tests the movement of the point towards South South East
    public void testSouthSouthEast() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-2.999942597, 56.1), 292.5, false);
    }

    // Tests the movement of the point towards East South East
    public void testEastSouthEast() {
        performDirectionTest2(new LngLat(-3.0, 56.0), new LngLat(-2.999861418, 56.00013858), 337.5, false);
    }

    private void performDirectionTest2(LngLat startingPosition, LngLat expectedNextPosition, double angle, boolean shouldPass) {
        LngLatHandler handler = new LngLatHandler();
        LngLat output = handler.nextPosition(startingPosition, angle);
        if (shouldPass) {
            assertTrue(handler.isCloseTo(output, expectedNextPosition));
        } else {
            assertFalse(handler.isCloseTo(output, expectedNextPosition));
        }
    }

    /**
     * Tests if a specific point is within the default Central Area.
     */
    public void testPointWithinCentralRegion() {
        LngLat testPoint = new LngLat(55.943, -3.1844);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };
        performRegionTest(testPoint, centralAreaVertices, CENTRAL_REGION_NAME, true);
    }

    /**
     * Tests if another point is within the default Central Area.
     */
    public void testAnotherPointWithinCentralRegion() {
        LngLat testPoint = new LngLat(55.9456, -3.1894);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };        performRegionTest(testPoint, centralAreaVertices, CENTRAL_REGION_NAME, true);
    }

    /**
     * Tests if a point is within a 7x7 square region defined as the Central Area.
     */
    public void testPointWithinDifferentVerticesRegion() {
        LngLat testPoint = new LngLat(3.0, 4.0);
        LngLat[] squareVertices = {
                new LngLat(0.0, 7.0),
                new LngLat(0.0, 0.0),
                new LngLat(7.0, 0.0),
                new LngLat(7.0, 7.0)
        };
        performRegionTest(testPoint, squareVertices, CENTRAL_REGION_NAME, true);
    }

    /**
     * Tests if a point on the edge of the default Central Area is considered inside.
     */
    public void testPointOnCentralRegionLongitudeEdge() {
        LngLat testPoint = new LngLat(55.946233, -3.1894);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };
        performRegionTest(testPoint, centralAreaVertices, CENTRAL_REGION_NAME, true);
    }

    /**
     * Tests if a point on another edge of the default Central Area is considered inside.
     */
    public void testPointOnCentralRegionLatitudeEdge() {
        LngLat testPoint = new LngLat(55.9456, -3.192473);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };
        performRegionTest(testPoint, centralAreaVertices, CENTRAL_REGION_NAME, true);
    }

    /**
     * Tests if a point on a vertex of the default Central Area is considered inside with differing name input.
     */
    public void testPointOnCentralRegionVertex() {
        LngLat testPoint = new LngLat(55.946233, -3.192473);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };

        performRegionTestWithException(testPoint, centralAreaVertices, "Central Area");
    }

    /**
     * Tests if a point at the vertex of a 7x7 square region is considered inside, with a differing name input.
     */
    public void testPointOnDifferentVerticesRegionVertex() {
        LngLat testPoint = new LngLat(7.0, 7.0);
        LngLat[] squareVertices = {
                new LngLat(0.0, 7.0),
                new LngLat(0.0, 0.0),
                new LngLat(7.0, 0.0),
                new LngLat(7.0, 7.0)
        };
        performRegionTestWithException(testPoint, squareVertices, "centralarea");
    }

    /**
     * Tests if a point outside the default Central Area is correctly identified as not being inside the region.
     */
    public void testPointOutsideCentralRegion() {
        LngLat testPoint = new LngLat(56.0, -3.2);
        LngLat[] centralAreaVertices = {
                new LngLat(55.946233, -3.192473),
                new LngLat(55.942617, -3.192473),
                new LngLat(55.942617, -3.184319),
                new LngLat(55.946233, -3.184319)
        };
        performRegionTest(testPoint, centralAreaVertices, CENTRAL_REGION_NAME, false);
    }

    /**
     * Tests if a point outside a 7x7 square region is correctly identified as not being inside the region.
     */
    public void testPointOutsideDifferentVerticesRegion() {
        LngLat testPoint = new LngLat(20.0, 10.0);
        LngLat[] squareVertices = {
                new LngLat(0.0, 7.0),
                new LngLat(0.0, 0.0),
                new LngLat(7.0, 0.0),
                new LngLat(7.0, 7.0)
        };
        performRegionTest(testPoint, squareVertices, CENTRAL_REGION_NAME, false);
    }

    /**
     * Helper method to perform the region test.
     */
    private void performRegionTest(LngLat point, LngLat[] vertices, String regionName, boolean expectedOutcome) {
        LngLatHandler handler = new LngLatHandler();
        NamedRegion region = new NamedRegion(regionName, vertices);
        boolean result = handler.isInCentralArea(point, region);
        if (expectedOutcome) {
            assertTrue(result);
        } else {
            assertFalse(result);
        }
    }

    /**
     * Helper method to perform the region test with exception handling.
     */
    private void performRegionTestWithException(LngLat point, LngLat[] vertices, String invalidRegionName) {
        LngLatHandler handler = new LngLatHandler();
        try {
            NamedRegion region = new NamedRegion(invalidRegionName, vertices);
            boolean result = handler.isInCentralArea(point, region);
            fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("the named region: " + invalidRegionName + " is not valid - must be: central", e.getMessage());
        }
    }
    /**
     * Tests if two close positions are considered close by the handler.
     */
    public void testProximityPythagorasValues() {
        LngLat firstPosition = new LngLat(0.0, 0.0);
        LngLat secondPosition = new LngLat(0.00003, 0.00004);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertTrue(arePositionsClose);
    }

    /**
     * Tests if two slightly more distant positions are still considered close by the handler.
     */
    public void testProximityNegativeValues() {
        LngLat firstPosition = new LngLat(-0.00001, -0.00001);
        LngLat secondPosition = new LngLat(-0.00007, -0.00009);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertTrue(arePositionsClose);
    }

    /**
     * Tests an edge case where two positions are exactly at the threshold distance.
     */
    public void testProximityEdgeCase() {
        LngLat firstPosition = new LngLat(0.0, 0.0);
        LngLat secondPosition = new LngLat(0.0, 0.00015);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertFalse(arePositionsClose);
    }

    /**
     * Tests if two identical positions are considered close.
     */
    public void testProximityZeroDistance() {
        LngLat firstPosition = new LngLat(0.0, 0.0);
        LngLat secondPosition = new LngLat(0.0, 0.0);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertTrue(arePositionsClose);
    }

    /**
     * Tests if two significantly distant positions are not considered close.
     */
    public void testProximityLargeValues() {
        LngLat firstPosition = new LngLat(3, 3);
        LngLat secondPosition = new LngLat(4, 5);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertFalse(arePositionsClose);
    }

    /**
     * Tests if a position with large values and a position with small values are not considered close.
     */
    public void testProximityLargeAndSmallValues() {
        LngLat firstPosition = new LngLat(3, 3);
        LngLat secondPosition = new LngLat(0.0003, 0.0004);
        LngLatHandler distanceHandler = new LngLatHandler();
        boolean arePositionsClose = distanceHandler.isCloseTo(firstPosition, secondPosition);
        assertFalse(arePositionsClose);
    }
}