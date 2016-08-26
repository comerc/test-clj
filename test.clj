(+ 1 2)

(def a {:b {:c 1}})

(get-in a [:b :c] "sorry")

(-> a :b :c (or "sorry"))

(take-last 1 [1 2 3 4 5])

(doc range)

(take 10 (iterate {:foo :foo} :foo))

(repeat 10 :foo)

(take 10 (iterate identity :foo))

(for [index (range 6)] (* index index))

(map (fn [index] (* index index)) (range 6))

((complement odd?) 1)

(odd? 1)

(map symbol? [:a 'b "c"])

(partial)

(let [f (partial + 100)] (f 1))

(hundred-times 2 2 2)

((partial concat [:a :b]) [:c])

(-> "a b c"
  (.toUpperCase)
  ((fn [v] (->> "X" (.replace v "A")))))

(-> "a" ((fn[v]{:k v})))
(-> "a" (#({:k %}))) -> clojure.lang.ArityException: Wrong number of args (0)
(-> "a" (#(identity {:k %})))
;; or
(-> "a" (#(hash-map :k %)))

(empty? [1])

(seq)

(vec #{1})

(= #{1} [1])

(def s [])

(macroexpand (not (empty? s)))

(hash-set 1 1
          2 3)

(array-map [3])


;; передача значения в макрос

(def x 25)

(defmacro my-macro1 []
  (let [xx 'x] ;; i.e. [xx (symbol "x")
    `(println ~xx)))

(my-macro1)

(macroexpand '(my-macro1))


;; генерация уникального символа для переменной в макросе

(defmacro my-macro2 []
  (let [xx (gensym "x")]
    `(let [~xx 28]
      (println ~xx))))

(macroexpand '(my-macro2))


;; сокращенная форма для gensym:

(defmacro my-macro3 []
  `(let [x# 28]
     (println x#)))

(macroexpand '(my-macro3))


;; @

(def cde [1 2 3])

(defmacro my-macro4 []
  `(println ~@cde)) ;; @ - раскрывает скобки

(macroexpand '(my-macro4))


;; логическая область видимости

(def b 10)

(defn my-func1 []
  (println b))

(my-func1) ;; вернет 10

(let [b 20]
  (my-func1)) ;; вернет по прежнему 10


;; динамическая область видимости

(def ^:dynamic *t* 50)

(defn my-func2 []
  (println *t*))

(my-func2) ;; вернет 50

(binding [*t* 10] (my-func2)) ;; вернет переопределенное 10

;; про замыкания

(defn my-cons [a b]
  (fn [get-first?]
    (if get-first?
      a
      b)))

;; просто анонимная функция не запомнит переменные a и b


;; Имеется семь основных абстракций, поддержимваемых реализациями структур данных в Clojure:
;; - коллекция
;; - последовательность
;; - ассоциативная коллекция
;; - индексируемая коллекция
;; - стек
;; - множество
;; - сортированная коллекция


;; макросы

(for-loop [(i 0) (< i 10) (inc i)] (println i))

(defmacro for-loop [[init test step] & body]
  `(loop [~@init] (when ~test ~@body (recur ~step))))

(macroexpand '(for-loop [(i 0) (< i 10) (inc i)] (println i)))



;; reader-macros in ClojureScript

;; #js []


;; полиморфизм:

;; - по количеству аргуметров

(defn greet
  ([name] (str "Hello " name))
  ([name1 name2] (str "Hello " name1 " and " name2)))

(defn fn-with-default-param
  ([param] (str param))
  ([] (fn-with-default-param "default")))

(fn-with-default-param)

;; - по значению (мультиметоды)

(defmulti diet (fn [x] (:eater x)))

(defmethod diet :wait [x] (str "value: " x))

;; (defmethod diet :default [a] "bambi")

(diet {:eater :wait})

;; - по типу (протоколы)

(defprotocol ArrayLike (push [coll val]))

(extend-type clojure.lang.PersistentVector ArrayLike (push [coll val] (conj coll val)))

(push [] 1)

(extend-type clojure.lang.PersistentHashSet ArrayLike (push [coll val] (conj coll val)))

(push #{} 1)

;; функция с произвольным количеством аргументов

(defn f [x & xs]
  xs)

(f 1 2 3)


;; локальная функция внутри нашего пространства имен

(defn- f [x]
  (+ 1 x))


;; пред- и пост- условия в функциях:

(def f ^{ :pre [(pos? x)]
          :post [(< % 20) (> % 10)]}
  [x]
  (+ 1 x))


;; функция, как точка возврата рекурсии (без loop)

(defn f [x]
  (println x)
  (recur (dec x)))


;; аргументы анонимной функции

#(inc %)

#(+ %1 %2)

#(str %1 %&)


;; apply и reduce

(def xs [1 2 3])

(= (apply + xs) (reduce + 0 xs))


;; juxt & comp

(def xs [1 2 3])

(map (juxt dec identity inc) xs)

(map (comp str inc) xs)


;; переходящие (мутабельные) структуры данных

(transient [])

(type (persistent! (transient [])))


;; Q: если функция возвращает другую функцию то это как назвать?
;; A: https://stuartsierra.com/2016/01/09/how-to-name-clojure-functions в конце про суффикс `fn`


(= '([:top :left] [:top :middle] [:top :right] [:middle :left] [:middle :middle] [:middle :right] [:bottom :left] [:bottom :middle] [:bottom :right])
   (for [row [:top :middle :bottom] column [:left :middle :right]] [row column]))


(let [not-nil? (complement nil?)]
  (filter not-nil? [nil :wheat nil 'wheat]))


((partial * 5) 4)

(concat [:a :b])

((fn [[a b]] (str b a)) [:foo :bar])

(let [[first-name last-name & aliases] (list "Rich" "Hickey" "The Clojure" "Go Time" "Macro Killah")]
  (str first-name " " last-name (apply str (map (partial str " aka ")  aliases))))

(let [[first-name last-name & aliases] (list "Rich" "Hickey" "The Clojure" "Go Time" "Macro Killah")]
  (str first-name " " last-name (apply str (for [al aliases] (str " aka " al)))))

(let [[first-name last-name & aliases] (list "Rich" "Hickey" "The Clojure" "Go Time" "Macro Killah")]
  (str first-name " " last-name " aka " (clojure.string/join " aka " aliases)))

(let [[first-name last-name & aliases] (list "Rich" "Hickey" "The Clojure" "Go Time" "Macro Killah")]
  (str first-name " " last-name " aka " (reduce (fn [acc cur] (str acc " aka " cur)) aliases)))

(let [[first-name last-name :as full-name] ["st" "h"]]
  {:original-parts full-name :named-parts {:first first-name :last last-name}})


(def test-addr
  {:s1 "111"
   :s2 "222"})

(let [{s1 :s1 s2 :s2} test-addr])

(let [{:keys [s1 s2]} test-addr] s1)

(def test-address
  {:street-address "123"
   :city "c"
   :state "s"})

((fn [a b] (str (clojure.string/join " " a) ", " (let [{:keys [street-address state city]} b] (str street-address ", " city ", " state))))
 ["Test" "Testerson"] test-address)

(+ 1 2 3 4 5)


;; чётное / нечётное

(odd? -1) => true
(even? 0) => true
(odd? 1) => true
(even? 2) => true


;; List comprehensions - Генератор списков

(for [x (range 1 10)
      y (range 1 10)]
  [x y])
