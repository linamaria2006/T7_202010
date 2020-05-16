package model.data_structures;


/**
 * Tomado de https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/MaxPQ.java
 */
import java.util.NoSuchElementException;

//Tomado de https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/AVLTreeST.java
public class AVLTreeST<Key extends Comparable<Key>, Value> implements IHashTable<Key, Value>{

	/**
	 * The root node.
	 */
	private NodoAVL raiz;

	/**
	 * This class represents an inner node of the AVL tree.
	 */
	private class NodoAVL 
	{
		private final Key key;   // the key
		private Value val;       // the associated value
		private int alt;      // height of the subtree
		private int size;        // number of nodes in subtree
		private NodoAVL izq;       // left subtree
		private NodoAVL der;      // right subtree

		public NodoAVL(Key key, Value val, int height, int size)
		{
			this.key = key;
			this.val = val;
			this.size = size;
			this.alt = height;
		}
	}

	/**
	 * Initializes an empty symbol table.
	 */
	public AVLTreeST()
	{
	}

	/**
	 * Checks if the symbol table is empty.
	 * 
	 * @return {@code true} if the symbol table is empty.
	 */
	public boolean isEmpty() 
	{
		return raiz == null;
	}

	/**
	 * Returns the number key-value pairs in the symbol table.
	 * 
	 * @return the number key-value pairs in the symbol table
	 */
	public int size()
	{
		return size(raiz);
	}

	/**
	 * Returns the number of nodes in the subtree.
	 * 
	 * @param nodoRaiz the subtree
	 * 
	 * @return the number of nodes in the subtree
	 */
	private int size(NodoAVL nodoRaiz) {
		if (nodoRaiz == null)
		{
			return 0;
		}
		return nodoRaiz.size;
	}

	/**
	 * Returns the height of the internal AVL tree. It is assumed that the
	 * height of an empty tree is -1 and the height of a tree with just one node
	 * is 0.
	 * 
	 * @return the height of the internal AVL tree
	 */
	public int altura()
	{
		return altura(raiz);
	}

	/**
	 * Returns the height of the subtree.
	 * 
	 * @param nodoRaiz the subtree
	 * 
	 * @return the height of the subtree.
	 */
	private int altura(NodoAVL nodoRaiz)
	{
		if (nodoRaiz == null) return -1;
		return nodoRaiz.alt;
	}

	/**
	 * Returns the value associated with the given key.
	 * 
	 * @param key the key
	 * @return the value associated with the given key if the key is in the
	 *         symbol table and {@code null} if the key is not in the
	 *         symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value get(Key key) {
		if (key == null)
		{
			throw new IllegalArgumentException("argumento para get() es null");
		}

		NodoAVL nodo = get(raiz, key);
		if (nodo == null) 
		{
			return null;
		}
		return nodo.val;
	}

	/**
	 * Returns value associated with the given key in the subtree or
	 * {@code null} if no such key.
	 * 
	 * @param nodo the subtree
	 * @param key the key
	 * @return value associated with the given key in the subtree or
	 *         {@code null} if no such key
	 */
	private NodoAVL get(NodoAVL nodo, Key key) {
		if (nodo == null)
		{
			return null;
		}

		int comparacion = key.compareTo(nodo.key);

		if (comparacion < 0)
		{
			return get(nodo.izq, key);
		}
		else if (comparacion > 0) 
		{
			return get(nodo.der, key);
		}
		else 
		{
			return nodo;
		}
	}

	/**
	 * Checks if the symbol table contains the given key.
	 * 
	 * @param key the key
	 * @return {@code true} if the symbol table contains {@code key}
	 *         and {@code false} otherwise
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public boolean contains(Key key)
	{
		return get(key) != null;
	}

	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting
	 * the old value with the new value if the symbol table already contains the
	 * specified key. Deletes the specified key (and its associated value) from
	 * this symbol table if the specified value is {@code null}.
	 * 
	 * @param key the key
	 * @param val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Key key, Value val) 
	{
		if (key == null)
		{
			throw new IllegalArgumentException("primer argumento para put() es null");
		}
		if (val == null) 
		{
			delete(key);
			return;
		}
		raiz = put(raiz, key, val);

		verificarInvariante();
	}

	/**
	 * Inserts the key-value pair in the subtree. It overrides the old value
	 * with the new value if the symbol table already contains the specified key
	 * and deletes the specified key (and its associated value) from this symbol
	 * table if the specified value is {@code null}.
	 * 
	 * @param nodo the subtree
	 * @param key the key
	 * @param val the value
	 * @return the subtree
	 */
	private NodoAVL put(NodoAVL nodo, Key key, Value val)
	{
		if (nodo == null)
		{
			return new NodoAVL(key, val, 0, 1);
		}
		int comparar = key.compareTo(nodo.key);
		if (comparar < 0) {
			nodo.izq = put(nodo.izq, key, val);
		}
		else if (comparar > 0) {
			nodo.der = put(nodo.der, key, val);
		}
		else {
			nodo.val = val;
			return nodo;
		}
		nodo.size = 1 + size(nodo.izq) + size(nodo.der);
		nodo.alt = 1 + Math.max(altura(nodo.izq), altura(nodo.der));
		return balancear(nodo);
	}

	/**
	 * Restores the AVL tree property of the subtree.
	 * 
	 * @param nodo the subtree
	 * @return the subtree with restored AVL property
	 */
	private NodoAVL balancear(NodoAVL nodo) 
	{
		if (balanceFactor(nodo) < -1) 
		{
			if (balanceFactor(nodo.der) > 0) 
			{
				nodo.der = rotarDer(nodo.der);
			}
			nodo = rotarIzq(nodo);
		}
		else if (balanceFactor(nodo) > 1) 
		{
			if (balanceFactor(nodo.izq) < 0) 
			{
				nodo.izq = rotarIzq(nodo.izq);
			}
			nodo = rotarDer(nodo);
		}
		return nodo;
	}

	/**
	 * Returns the balance factor of the subtree. The balance factor is defined
	 * as the difference in height of the left subtree and right subtree, in
	 * this order. Therefore, a subtree with a balance factor of -1, 0 or 1 has
	 * the AVL property since the heights of the two child subtrees differ by at
	 * most one.
	 * 
	 * @param x the subtree
	 * @return the balance factor of the subtree
	 */
	private int balanceFactor(NodoAVL x) 
	{
		return altura(x.izq) - altura(x.der);
	}

	/**
	 * Rotates the given subtree to the right.
	 * 
	 * @param x the subtree
	 * @return the right rotated subtree
	 */
	private NodoAVL rotarDer(NodoAVL x) {
		NodoAVL y = x.izq;
		x.izq = y.der;
		y.der = x;
		y.size = x.size;
		x.size = 1 + size(x.izq) + size(x.der);
		x.alt = 1 + Math.max(altura(x.izq), altura(x.der));
		y.alt = 1 + Math.max(altura(y.izq), altura(y.der));
		return y;
	}

	/**
	 * Rotates the given subtree to the left.
	 * 
	 * @param x the subtree
	 * @return the left rotated subtree
	 */
	private NodoAVL rotarIzq(NodoAVL x) {
		NodoAVL y = x.der;
		x.der = y.izq;
		y.izq = x;
		y.size = x.size;
		x.size = 1 + size(x.izq) + size(x.der);
		x.alt = 1 + Math.max(altura(x.izq), altura(x.der));
		y.alt = 1 + Math.max(altura(y.izq), altura(y.der));
		return y;
	}

	/**
	 * Removes the specified key and its associated value from the symbol table
	 * (if the key is in the symbol table).
	 * 
	 * @param key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void delete(Key key) {
		if (key == null)
		{
			throw new IllegalArgumentException("argument to delete() is null");
		}
		if (!contains(key))
		{
			return;
		}
		raiz = delete(raiz, key);

		verificarInvariante();
	}

	/**
	 * Removes the specified key and its associated value from the given
	 * subtree.
	 * 
	 * @param x the subtree
	 * @param key the key
	 * @return the updated subtree
	 */
	private NodoAVL delete(NodoAVL x, Key key)
	{
		int comparacion = key.compareTo(x.key);
		if (comparacion < 0) {
			x.izq = delete(x.izq, key);
		}
		else if (comparacion > 0) {
			x.der = delete(x.der, key);
		}
		else {
			if (x.izq == null) 
			{
				return x.der;
			}
			else if (x.der == null) 
			{
				return x.izq;
			}
			else
			{
				NodoAVL y = x;
				x = min(y.der);
				x.der = deleteMin(y.der);
				x.izq = y.izq;
			}
		}
		x.size = 1 + size(x.izq) + size(x.der);
		x.alt = 1 + Math.max(altura(x.izq), altura(x.der));
		return balancear(x);
	}

	/**
	 * Removes the smallest key and associated value from the symbol table.
	 * 
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMin() 
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("called deleteMin() with empty symbol table");
		}
		raiz = deleteMin(raiz);

		verificarInvariante();
	}

	/**
	 * Removes the smallest key and associated value from the given subtree.
	 * 
	 * @param x the subtree
	 * @return the updated subtree
	 */
	private NodoAVL deleteMin(NodoAVL x)
	{
		if (x.izq == null)
		{
			return x.der;
		}
		x.izq = deleteMin(x.izq);
		x.size = 1 + size(x.izq) + size(x.der);
		x.alt = 1 + Math.max(altura(x.izq), altura(x.der));
		return balancear(x);
	}

	/**
	 * Removes the largest key and associated value from the symbol table.
	 * 
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMax() {
		if (isEmpty())
		{
			throw new NoSuchElementException("called deleteMax() with empty symbol table");
		}
		raiz = deleteMax(raiz);
		verificarInvariante();
	}

	/**
	 * Removes the largest key and associated value from the given subtree.
	 * 
	 * @param x the subtree
	 * @return the updated subtree
	 */
	private NodoAVL deleteMax(NodoAVL x)
	{
		if (x.der == null)
		{
			return x.izq;
		}
		x.der = deleteMax(x.der);
		x.size = 1 + size(x.izq) + size(x.der);
		x.alt = 1 + Math.max(altura(x.izq), altura(x.der));
		return balancear(x);
	}

	/**
	 * Returns the smallest key in the symbol table.
	 * 
	 * @return the smallest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Key min() {
		if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
		return min(raiz).key;
	}

	/**
	 * Returns the node with the smallest key in the subtree.
	 * 
	 * @param x the subtree
	 * @return the node with the smallest key in the subtree
	 */
	private NodoAVL min(NodoAVL x)
	{
		if (x.izq == null)
		{
			return x;
		}
		return min(x.izq);
	}

	/**
	 * Returns the largest key in the symbol table.
	 * 
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Key max()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("called max() with empty symbol table");
		}
		return max(raiz).key;
	}

	/**
	 * Returns the node with the largest key in the subtree.
	 * 
	 * @param x the subtree
	 * @return the node with the largest key in the subtree
	 */
	private NodoAVL max(NodoAVL x)
	{
		if (x.der == null)
		{
			return x;
		}
		return max(x.der);
	}

	/**
	 * Returns the largest key in the symbol table less than or equal to
	 * {@code key}.
	 * 
	 * @param key the key
	 * @return the largest key in the symbol table less than or equal to
	 *         {@code key}
	 * @throws NoSuchElementException if the symbol table is empty
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Key floor(Key key)
	{
		if (key == null) 
		{
			throw new IllegalArgumentException("argument to floor() is null");
		}
		if (isEmpty()) 
		{
			throw new NoSuchElementException("called floor() with empty symbol table");
		}
		NodoAVL x = floor(raiz, key);
		if (x == null)
		{
			return null;
		}
		else
		{
			return x.key;
		}
	}

	/**
	 * Returns the node in the subtree with the largest key less than or equal
	 * to the given key.
	 * 
	 * @param x the subtree
	 * @param key the key
	 * @return the node in the subtree with the largest key less than or equal
	 *         to the given key
	 */
	private NodoAVL floor(NodoAVL x, Key key) 
	{
		if (x == null)
		{
			return null;
		}
		int comparacion = key.compareTo(x.key);
		if (comparacion == 0) 
		{
			return x;
		}
		if (comparacion < 0) 
		{
			return floor(x.izq, key);
		}
		NodoAVL y = floor(x.der, key);
		if (y != null)
		{
			return y;
		}
		else return x;
	}

	/**
	 * Returns the smallest key in the symbol table greater than or equal to
	 * {@code key}.
	 * 
	 * @param key the key
	 * @return the smallest key in the symbol table greater than or equal to
	 *         {@code key}
	 * @throws NoSuchElementException if the symbol table is empty
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Key ceiling(Key key) {
		if (key == null)
		{
			throw new IllegalArgumentException("argument to ceiling() is null");
		}
		if (isEmpty()) 
		{
			throw new NoSuchElementException("called ceiling() with empty symbol table");
		}
		NodoAVL x = ceiling(raiz, key);
		if (x == null) 
		{
			return null;
		}
		else 
		{
			return x.key;
		}
	}

	/**
	 * Returns the node in the subtree with the smallest key greater than or
	 * equal to the given key.
	 * 
	 * @param x the subtree
	 * @param key the key
	 * @return the node in the subtree with the smallest key greater than or
	 *         equal to the given key
	 */
	private NodoAVL ceiling(NodoAVL x, Key key)
	{
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) 
		{
			return x;
		}
		if (cmp > 0) 
		{
			return ceiling(x.der, key);
		}
		NodoAVL y = ceiling(x.izq, key);
		if (y != null) 
		{
			return y;
		}
		else 
		{
			return x;
		}
	}

	/**
	 * Returns the kth smallest key in the symbol table.
	 * 
	 * @param k the order statistic
	 * @return the kth smallest key in the symbol table
	 * @throws IllegalArgumentException unless {@code k} is between 0 and
	 *             {@code size() -1 }
	 */
	public Key select(int k) 
	{
		if (k < 0 || k >= size()) 
		{
			throw new IllegalArgumentException("k is not in range 0-" + (size() - 1));
		}
		NodoAVL x = select(raiz, k);
		return x.key;
	}

	/**
	 * Returns the node with key the kth smallest key in the subtree.
	 * 
	 * @param x the subtree
	 * @param k the kth smallest key in the subtree
	 * @return the node with key the kth smallest key in the subtree
	 */
	private NodoAVL select(NodoAVL x, int k) 
	{
		if (x == null)
		{
			return null;
		}
		int t = size(x.izq);
		if (t > k)
		{
			return select(x.izq, k);
		}
		else if (t < k)
		{
			return select(x.der, k - t - 1);
		}
		else
		{
			return x;
		}
	}

	/**
	 * Returns the number of keys in the symbol table strictly less than
	 * {@code key}.
	 * 
	 * @param key the key
	 * @return the number of keys in the symbol table strictly less than
	 *         {@code key}
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public int rank(Key key) 
	{
		if (key == null)
		{
			throw new IllegalArgumentException("argument to rank() is null");
		}
		return rank(key, raiz);
	}

	/**
	 * Returns the number of keys in the subtree less than key.
	 * 
	 * @param key the key
	 * @param x the subtree
	 * @return the number of keys in the subtree less than key
	 */
	private int rank(Key key, NodoAVL x)
	{
		if (x == null)
		{
			return 0;
		}
		int cmp = key.compareTo(x.key);
		if (cmp < 0) 
		{
			return rank(key, x.izq);
		}
		else if (cmp > 0) 
		{
			return 1 + size(x.izq) + rank(key, x.der);
		}
		else
		{
			return size(x.izq);
		}
	}

	/**
	 * Returns all keys in the symbol table.
	 * 
	 * @return all keys in the symbol table
	 */
	public Iterable<Key> keys() {
		return keysInOrder();
	}

	/**
	 * Returns all keys in the symbol table following an in-order traversal.
	 * 
	 * @return all keys in the symbol table following an in-order traversal
	 */
	public Iterable<Key> keysInOrder() {
		Queue<Key> queue = new Queue<Key>();
		keysInOrder(raiz, queue);
		return queue;
	}

	/**
	 * Adds the keys in the subtree to queue following an in-order traversal.
	 * 
	 * @param x the subtree
	 * @param queue the queue
	 */
	private void keysInOrder(NodoAVL x, Queue<Key> queue) {
		if (x == null) 
		{
			return;
		}
		keysInOrder(x.izq, queue);
		queue.enqueue(x.key);
		keysInOrder(x.der, queue);
	}

	/**
	 * Returns all keys in the symbol table following a level-order traversal.
	 * 
	 * @return all keys in the symbol table following a level-order traversal.
	 * @throws noExisteObjetoException 
	 */
	public Iterable<Key> keysLevelOrder() throws noExisteObjetoException {

		Queue<Key> queue = new Queue<Key>();

		if (!isEmpty())
		{
			Queue<NodoAVL> queue2 = new Queue<NodoAVL>();
			queue2.enqueue(raiz);
			while (!queue2.isEmpty())
			{
				NodoAVL x = queue2.dequeue();
				queue.enqueue(x.key);
				if (x.izq != null)
				{
					queue2.enqueue(x.izq);
				}
				if (x.der != null) 
				{
					queue2.enqueue(x.der);
				}
			}
		}
		return queue;
	}

	/**
	 * Returns all keys in the symbol table in the given range.
	 * 
	 * @param min the lowest key
	 * @param max the highest key
	 * @return all keys in the symbol table between {@code lo} (inclusive)
	 *         and {@code hi} (exclusive)
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi}
	 *             is {@code null}
	 */
	public Iterable<Key> keys(Key min, Key max)
	{
		if (min == null)
		{
			throw new IllegalArgumentException("first argument to keys() is null");
		}
		if (max == null)
		{
			throw new IllegalArgumentException("second argument to keys() is null");
		}
		Queue<Key> queue = new Queue<Key>();
		keys(raiz, queue, min, max);
		return queue;
	}

	/**
	 * Adds the keys between {@code lo} and {@code hi} in the subtree
	 * to the {@code queue}.
	 * 
	 * @param x the subtree
	 * @param queue the queue
	 * @param min the lowest key
	 * @param max the highest key
	 */
	private void keys(NodoAVL x, Queue<Key> queue, Key min, Key max) 
	{
		if (x == null)
		{
			return;
		}
		int compMin = min.compareTo(x.key);
		int compMax = max.compareTo(x.key);
		if (compMin < 0)
		{
			keys(x.izq, queue, min, max);
		}
		if (compMin <= 0 && compMax >= 0) 
		{
			queue.enqueue(x.key);
		}
		if (compMax > 0)
		{
			keys(x.der, queue, min, max);
		}
	}

	/**
	 * Returns the number of keys in the symbol table in the given range.
	 * 
	 * @param min minimum endpoint
	 * @param max maximum endpoint
	 * @return the number of keys in the symbol table between {@code lo}
	 *         (inclusive) and {@code hi} (exclusive)
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi}
	 *             is {@code null}
	 */
	public int size(Key min, Key max) 
	{
		if (min == null) 
		{
			throw new IllegalArgumentException("first argument to size() is null");
		}
		if (max == null)
		{
			throw new IllegalArgumentException("second argument to size() is null");
		}
		if (min.compareTo(max) > 0)
		{
			return 0;
		}
		if (contains(max))
		{
			return rank(max) - rank(min) + 1;
		}
		else
		{
			return rank(max) - rank(min);
		}
	}

	/**
	 * Checks if the AVL tree invariants are fine.
	 * 
	 * @return {@code true} if the AVL tree invariants are fine
	 */
	private void verificarInvariante() 
	{
		assert esBST() : "el orden simetrico no es consistente";
		assert esAVL(): "La propiedad AVL no es consistente";
		assert tamanoConsistente() : "el subarbol no es consistente";
		assert elRangoEsConsistente() : "Ranks no consistentes";
	}

	/**
	 * Checks if AVL property is consistent.
	 * 
	 * @return {@code true} if AVL property is consistent.
	 */
	private boolean esAVL() {
		return esAVL(raiz);
	}

	/**
	 * Checks if AVL property is consistent in the subtree.
	 * 
	 * @param x the subtree
	 * @return {@code true} if AVL property is consistent in the subtree
	 */
	private boolean esAVL(NodoAVL x) {
		if (x == null)
		{
			return true;
		}
		int bf = balanceFactor(x);
		if (bf > 1 || bf < -1) 
		{
			return false;
		}
		return esAVL(x.izq) && esAVL(x.der);
	}

	/**
	 * Checks if the symmetric order is consistent.
	 * 
	 * @return {@code true} if the symmetric order is consistent
	 */
	private boolean esBST() {
		return esBST(raiz, null, null);
	}

	/**
	 * Checks if the tree rooted at x is a BST with all keys strictly between
	 * min and max (if min or max is null, treat as empty constraint) Credit:
	 * Bob Dondero's elegant solution
	 * 
	 * @param x the subtree
	 * @param min the minimum key in subtree
	 * @param max the maximum key in subtree
	 * @return {@code true} if if the symmetric order is consistent
	 */
	private boolean esBST(NodoAVL x, Key min, Key max) {
		if (x == null)
		{
			return true;
		}
		if (min != null && x.key.compareTo(min) <= 0) 
		{
			return false;
		}
		if (max != null && x.key.compareTo(max) >= 0)
		{
			return false;
		}
		return esBST(x.izq, min, x.key) && esBST(x.der, x.key, max);
	}

	/**
	 * Checks if size is consistent.
	 * 
	 * @return {@code true} if size is consistent
	 */
	private boolean tamanoConsistente() {
		return tamanoConsistente(raiz);
	}

	/**
	 * Checks if the size of the subtree is consistent.
	 * 
	 * @return {@code true} if the size of the subtree is consistent
	 */
	private boolean tamanoConsistente(NodoAVL x) {
		if (x == null)
		{
			return true;
		}
		if (x.size != size(x.izq) + size(x.der) + 1)
		{
			return false;
		}
		return tamanoConsistente(x.izq) && tamanoConsistente(x.der);
	}

	/**
	 * Checks if rank is consistent.
	 * 
	 * @return {@code true} if rank is consistent
	 */
	private boolean elRangoEsConsistente() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i))) return false;
		for (Key key : keys())
			if (key.compareTo(select(rank(key))) != 0) return false;
		return true;
	}

	/**
	 * Unit tests the {@code AVLTreeST} data type.
	 *
	 * @param args the command-line arguments
	 */


}