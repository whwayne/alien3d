package net.age.mvs.physics2d;

public class RigidBody {

	private static final int numberOfConfigurations = 2;
	
	float width, height;
	float oneOverMass, oneOverCMMomentOfInertia;
	float coefficientOfRestitution;
	
	Configuration[] aConfigurations = new Configuration[numberOfConfigurations];
	
}
