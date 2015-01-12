package kodkod_helper;

import java.util.Vector;

import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;

public class Helper {

  public static TupleSet getTupleSet(TupleFactory factory, Vector<Tuple> tuples) {
    if(tuples.isEmpty())
      return factory.setOf();
    else return factory.setOf(tuples);
  }
  
  public static Object[] tuple2Array(Tuple t) {
    int arity = t.arity();
    Object[] s = new Object[arity];
    for(int i = 0; i < arity; i++)
      s[i] = t.atom(i);
    return s;
  }

}
