package model;

public class ClonePair {
	
	public ClonePair(int similarity, int number_of_lines, Clone first_clone, Clone second_clone) {
		this.similarity = similarity;
		this.number_of_lines = number_of_lines;
		this.first_clone = first_clone;
		this.second_clone = second_clone;
	}

	public int getSimilarity() {
		return similarity;
	}
	public void setSimilarity(int similarity) {
		this.similarity = similarity;
	}
	public int getNumber_of_lines() {
		return number_of_lines;
	}
	public void setNumber_of_lines(int number_of_lines) {
		this.number_of_lines = number_of_lines;
	}
	public Clone getFirst_clone() {
		return first_clone;
	}
	public void setFirst_clone(Clone first_clone) {
		this.first_clone = first_clone;
	}
	public Clone getSecond_clone() {
		return second_clone;
	}
	public void setSecond_clone(Clone second_clone) {
		this.second_clone = second_clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first_clone == null) ? 0 : first_clone.hashCode());
		result = prime * result + number_of_lines;
		result = prime * result + ((second_clone == null) ? 0 : second_clone.hashCode());
		result = prime * result + similarity;
		return result;
	}

	//RETURN TRUE if:
	//-	CLONEPAIR1 == CLONEPAIR2(SAME OBJECT)
	//-	if (CLONEPAIR1.CLONE1 == CLONEPAIR2.CLONE1) && (CLONEPAIR1.CLONE2 == CLONEPAIR2.CLONE2) + ->with SIMILARITY & NUMLINES
	//-	if (CLONEPAIR1.CLONE1 == CLONEPAIR2.CLONE2) && (CLONEPAIR1.CLONE2 == CLONEPAIR2.CLONE1) + ->with SIMILARITY & NUMLINES
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		ClonePair other = (ClonePair) obj;

		if (similarity != other.similarity)
			return false;
		if (number_of_lines != other.number_of_lines)
			return false;
		
		//AVOID USING -EQUALS- FOR NULL VALUES
		if (first_clone == null)
		{
			//NOTE:FIRST1 == NULL
			if (other.first_clone == null)
			{
				//FIRST1 == FIRST2
				//NEDDED SECOND1 == SECOND2
				if(second_clone == null)
					return (other.second_clone == null);
				else
				{
					//NOTE:SECOND1 != NULL
					if(other.second_clone == null)
						return false;
					else
						return (second_clone.equals(other.second_clone));
				}
			}
			else
			{
				//FIRST1 != FIRST2
				//NEDDED FIRST1 == SECOND2
				//NEDDED SECOND1 == FIRST2
				//NOTE:FIRST1 == NULL
				if(other.second_clone != null)
					return false;
				else
				{
					//FIRST1 == SECOND2
					//NEDDED SECOND1 == FIRST2
					if(second_clone == null)
						return (other.first_clone == null);
					else
					{
						//NOTE:SECOND1 != NULL
						if(other.first_clone == null)
							return false;
						else
							return second_clone.equals(other.first_clone);
					}
				}
			}
		}
		else
		{
			//NOTE:FIRST1 != NULL
			if (other.first_clone == null)
			{
				//FIRST1 != FIRST2
				//NEDDED FIRST1 == SECOND2
				//NEDDED SECOND1 == FIRST2
				//NOTE:FIRST2 == NULL
				if(second_clone != null)
					return false;
				else
				{
					//SECOND1 == FIRST2
					//NEDDED FIRST1 == SECOND2
					//NOTE:FIRST1 != NULL
					if(other.second_clone == null)
						return false;
					else
						return (first_clone.equals(other.second_clone));
				}
			}
			else
			{
				//NOTE:FIRST1 != NULL
				//NOTE:FIRST2 != NULL
				if(first_clone.equals(other.first_clone))
				{
					//FIRST1 == FIRST2
					//NEDDED SECOND1 == SECOND2
					if(second_clone == null)
						return (other.second_clone == null);
					else
					{
						//NOTE:SECOND1 != NULL
						if(other.second_clone == null)
							return false;
						else
							return (second_clone.equals(other.second_clone));
					}
				}
				else
				{
					//FIRST1 != FIRST2
					//NEDDED FIRST1 == SECOND2
					//NEDDED SECOND1 == FIRST2
					//NOTE:FIRST1 != NULL
					//NOTE:FIRST2 != NULL
					if(other.second_clone == null)
						return false;
					else
					{
						//NOTE:SECOND2 != NULL
						if(first_clone.equals(other.second_clone))
						{
							//FIRST1 == SECOND2
							//NEDDED SECOND1 == FIRST2
							//NOTE:FIRST2 != NULL
							if(second_clone == null)
								return false;
							else
								return (second_clone.equals(other.first_clone));
						}
						else
							return false;
					}
				}
			}
		}
	}

	private int similarity;
	private int number_of_lines;
	private Clone first_clone = null;
	private Clone second_clone = null;

}
