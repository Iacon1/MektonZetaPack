// By Iacon1
// Created 12/02/2021
// A value with a scale it's measured at that can be converted to other scales

package Modules.MektonCore.StatsStuff.ScaledUnits;

import Modules.MektonCore.Enums.Scale;

/** A scaled value. There's a very simple way to
 *  implement these abstract functions but
 *  for type safety reasons they have to be abstract.
 *  
 *  getRatio(a, b): Listed in its documenation.
 *  copy(): Just a copy of this T.
 *  
 *  @param T The class of the subclass extending this.
 */
public abstract class ScaledValue<T extends ScaledValue<T>>
{
	protected static final Scale defaultScale = Scale.mekton;
	
	private Scale scale; // The given scale
	private double value; // Value in the given scale

	/** Returns the ratio r between scale a and b, such that
	 *  X a = Y b -> Y = r * X
	 *
	 *  One way to implement this is to establish a base scale and measure other's values off of that.
	 *  For mekton's purposes, the base scale is mekton scale.
	 *  
	 *  Example:
	 *    A yard (scale a) is 3 feet (Assuming feet to be the "standard" scale)
	 *    A mile (scale b) is 5280 feet
	 *    
	 *    10 yards is (a / b) * X = 3 / 5280 * 10 = Y = .0057 miles.
	 *    So r = 3 / 5280 = 0.0006, which is what this function would return.
	 *    
	 *    @return the ratio between scale a and b, as given above.
	 */
	protected abstract double getRatio(Scale a, Scale b);

	/** Returns a copy of this value.
	 *  @return A copy of this value.
	 */
	protected abstract T copy();
	
	// Constructors
	/** Copy constructor. */
	protected ScaledValue(T value)
	{
		this.scale = value.getScale();
		setValue(value);
	}
	protected ScaledValue(Scale scale, double value)
	{
		this.scale = scale;
		this.value = value;
	}
	protected ScaledValue(Scale scale)
	{
		this.scale = scale;
		this.value = -1;
	}
	protected ScaledValue(double value)
	{
		this.scale = defaultScale;
		this.value = value;
	}
	protected ScaledValue()
	{
		scale = defaultScale;
		value = -1;
	}
	// Getters
	/** Returns the default scale of this value. 
	 *  @return The default scale of this value.
	 */
	public Scale getScale() {return scale;}	
	/** Returns the value of this value at the specified scale. 
	 *  
	 *  @param The scale to return at.
	 *  @return The value at that scale.
	 */
	public double getValue(Scale scale)
	{
		return value * getRatio(this.scale, scale);
	}
	/** Returns the value of this value at the default scale (Mekton-scale). 
	 * 
	 *  @return The value at default scale.
	 */
	public double getValue()
	{
		return getValue(defaultScale);
	}
	
	// Setter
	/** Sets this value to target, maintaining the current scale.
	 * 
	 *  @param value The value to set this one to.
	 */
	public void setValue(T target)
	{
		value = target.getValue(scale);
	}
	
	// In-place operators
	/** Adds value delta to this value, converting units along the way.
	 * 
	 *  @param delta The value to add to this one.
	 */
	public void addInPlace(T delta)
	{
		this.value += delta.getValue(scale);
	}
	/** Subtracts value delta to this value, converting units along the way.
	 * 
	 *  @param delta The value to add to this one.
	 */
	public void subtractInPlace(T delta)
	{
		this.value -= delta.getValue(scale);
	}

	// Non-in-place operators
	/** Returns result of this plus delta, using this value's scale.
	 * 
	 *  @param delta The value to add.
	 */
	public T add(T delta)
	{
		T copy = copy();
		copy.addInPlace(delta);
		return copy;
	}
	/** Returns result of this minus delta, using this value's scale.
	 * 
	 *  @param delta The value to subtract.
	 */
	public T subtract(T delta)
	{
		T copy = copy();
		copy.subtractInPlace(delta);
		return copy;
	}
	public T multiply(double factor)
	{
		double oldValue = value;
		value *= factor;
		T copy = copy();
		value = oldValue;
		
		return copy;
	}
	public T divide(double factor)
	{
		double oldValue = value;
		value /= factor;
		T copy = copy();
		value = oldValue;
		
		return copy;
	}
	
	// Comparators
	/** Returns whether or not this is less than b.
	 * 
	 *  @param b The value to check against.
	 *  
	 *  @return 1 if this is less than b and 0 otherwise.
	 */
	public boolean lessThan(T b)
	{
		return value < b.getValue(scale);
	}
	/** Returns whether or not this equals b.
	 * 
	 *  @param b The value to check against.
	 *  
	 *  @return 1 if this is equal to b and 0 otherwise.
	 */
	public boolean equals(T b)
	{
		return value == b.getValue(scale);
	}
	/** Returns whether or not this is greater than b.
	 * 
	 *  @param b The value to check against.
	 *  
	 *  @return 1 if this is greater than b and 0 otherwise.
	 */
	public boolean greaterThan(T b)
	{
		return value > b.getValue(scale);
	}
}
