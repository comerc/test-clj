; Комментарии начинаются символом ";".

; Код на языке Clojure записывается в виде "форм",
; которые представляют собой обычные списки элементов, разделенных пробелами,
; заключённые в круглые скобки
;
; Clojure Reader (инструмент языка, отвечающий за чтение исходного кода),
; анализируя форму, предполагает, что первым элементом формы (т.е. списка)
; является функция или макрос, который следует вызвать, передав ему
; в качестве аргументов остальные элементы списка-формы.

; Первым вызовом в файле должен быть вызов функции ns,
; которая отвечает за выбор текущего пространства имен (namespace)
(ns learnclojure-ru)

; Несколько простых примеров:

; str объединяет в единую строку все свои аргументы
(str "Hello" " " "World") ; => "Hello World"

; Арифметика тоже выглядит несложно
(+ 1 1) ; => 2
(- 2 1) ; => 1
(* 1 2) ; => 2
(/ 2 1) ; => 2

; Проверка на равенство (Equality)
(= 1 1) ; => true
(= 2 1) ; => false

; Для булевой логики вам может понадобиться not
(not true) ; => false

; Вложенные формы, конечно же, допустимы и работают вполне предсказуемо
(+ 1 (- 3 2)) ; = 1 + (3 - 2) => 2

; Типы
;;;;;;;;;;;;;

; Clojure использует типы Java для представления булевых значений,
; строк и чисел
; Узнать тип мы можем, использую функцию `class
(class 1)     ; Целочисленные литералы типа по-умолчанию являются java.lang.Long
(class 1.)    ; Числа с плавающей точкой, это java.lang.Double
(class "")    ; Строки всегда заключаются в двойные кавычки
              ; и представляют собой java.lang.String
(class false) ; Булевы значения, это экземпляры java.lang.Boolean
(class nil)   ; "Пустое" значение называется "nil"

; Если Вы захотите создать список из чисел, вы можете просто
; предварить форму списка символом "'", который подскажет Reader`у,
; что эта форма не требует вычисления
'(+ 1 2) ; => (+ 1 2)
; ("'", это краткая запись формы (quote (+ 1 2))

; "Квотированный" список можно вычислить, передав его функции eval
(eval '(+ 1 2)) ; => 3

; Коллекции и Последовательности
;;;;;;;;;;;;;;;;;;;

; Списки (Lists) в clojure структурно представляют собой "связанные списки",
; тогда как Векторы (Vectors), устроены как массивы.
; Векторы и Списки тоже являются классами Java!
(class [1 2 3]); => clojure.lang.PersistentVector
(class '(1 2 3)); => clojure.lang.PersistentList

; Список может быть записан, как (1 2 3), но в этом случае
; он будет воспринят reader`ом, как вызов функции.
; Есть два способа этого избежать:
; '(1 2 3)     - квотирование,
; (list 1 2 3) - явное конструирование списка с помощью функции list.

; "Коллекции", это некие наборы данных
; И списки, и векторы являются коллекциями:
(coll? '(1 2 3)) ; => true
(coll? [1 2 3]) ; => true

; "Последовательности" (seqs), это абстракция над наборами данных,
; элементы которых "упакованы" последовательно.
; Списки - последовательности, а вектора - нет.
(seq? '(1 2 3)) ; => true
(seq? [1 2 3]) ; => false

; Любая seq предоставляет доступ только к началу последовательности данных,
; не предоставляя информацию о её длине.
; При этом последовательности могут быть и бесконечными,
; т.к. являются ленивыми и предоставляют данные только по требованию!
(range 4) ; => (0 1 2 3)
(range) ; => (0 1 2 3 4 ...) (бесконечная последовательность!)
(take 4 (range)) ;  (0 1 2 3)

; Добавить элемент в начало списка или вектора можно с помощью функции cons
(cons 4 [1 2 3]) ; => (4 1 2 3)
(cons 4 '(1 2 3)) ; => (4 1 2 3)

; Функция conj добавляет элемент в коллекцию
; максимально эффективным для неё способом.
; Для списков эффективно добавление в начло, а для векторов - в конец.
(conj [1 2 3] 4) ; => [1 2 3 4]
(conj '(1 2 3) 4) ; => (4 1 2 3)

; Функция concat объединяет несколько списков и векторов в единый список
(concat [1 2] '(3 4)) ; => (1 2 3 4)

; Работать с коллекциями удобно с помощью функций filter и map
(map inc [1 2 3]) ; => (2 3 4)
(filter even? [1 2 3]) ; => (2)

; reduce поможет "свернуть" коллекцию
(reduce + [1 2 3 4])
; = (+ (+ (+ 1 2) 3) 4)
; => 10

; Вызывая reduce, мы можем указать начальное значение
(reduce conj [] '(3 2 1))
; = (conj (conj (conj [] 3) 2) 1)
; => [3 2 1]

; Функции
;;;;;;;;;;;;;;;;;;;;;

; Функция создается специальной формой fn.
; "Тело" функции может состоять из нескольких форм,
; но результатом вызова функции всегда будет результат вычисления
; последней из них.
(fn [] "Hello World") ; => fn

; (Вызов функции требует "оборачивания" fn-формы в форму вызова)
((fn [] "Hello World")) ; => "Hello World"

; Назначить значению имя можно специальной формой def
(def x 1)
x ; => 1

; Назначить имя можно любому значению, в т.ч. и функции:
(def hello-world (fn [] "Hello World"))
(hello-world) ; => "Hello World"

; Поскольку именование функций - очень частая операция,
; clojure позволяет, сделать это проще:
(defn hello-world [] "Hello World")

; Вектор [] в форме описания функции, следующий сразу за именем,
; описывает параметры функции:
(defn hello [name]
  (str "Hello " name))
(hello "Steve") ; => "Hello Steve"

; Одна функция может иметь сразу несколько наборов аргументов:
(defn hello3
  ([] "Hello World")
  ([name] (str "Hello " name)))
(hello3 "Jake") ; => "Hello Jake"
(hello3) ; => "Hello World"

; Также функция может иметь набор аргументов переменной длины
(defn count-args [& args] ; args будет содержать seq аргументов
  (str "You passed " (count args) " args: " args))
(count-args 1 2 3) ; => "You passed 3 args: (1 2 3)"

; Можно комбинировать оба подхода задания аргументов
(defn hello-count [name & args]
  (str "Hello " name ", you passed " (count args) " extra args"))
(hello-count "Finn" 1 2 3)
; => "Hello Finn, you passed 3 extra args"

; Для создания анонимных функций есть специальный синтаксис:
; функциональные литералы
(def hello2 #(str "Hello " %1))
(hello2 "Fanny") ; => "Hello Fanny"

; такие функциональные литералы удобно использовать с map, filter и reduce
(map #(* 10 %1) [1 2 3 5])          ; => (10 20 30 50)
(filter #(> %1 3) [1 2 3 4 5 6 7])  ; => (4 5 6 7)
(reduce #(str %1 "," %2) [1 2 3 4]) ; => "1,2,3,4"

; Отображения (Maps)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Hash maps и array maps имеют одинаковый интерфейс.
; Hash maps производят поиск по ключу быстрее, но не сохраняют порядок ключей
(class {:a 1 :b 2 :c 3})          ; => clojure.lang.PersistentArrayMap
(class (hash-map :a 1 :b 2 :c 3)) ; => clojure.lang.PersistentHashMap

; Array maps автоматически преобразуются в hash maps,
; как только разрастутся до определенного размера

; Отображения могут использовать в качестве ключей любые хэшируемые значения,
; однако предпочтительными являются ключи,
; являющиеся "ключевыми словами" (keywords)
(class :a) ; => clojure.lang.Keyword

(def stringmap {"a" 1, "b" 2, "c" 3})
stringmap  ; => {"a" 1, "b" 2, "c" 3}

(def keymap {:a 1, :b 2, :c 3})
keymap ; => {:a 1, :c 3, :b 2}

; Предыдущий пример содержит запятые в коде, однако reader не использует их,
; при обработке литералов - запятые просто воспринимаются,
; как "пробельные символы" (whitespaces)

; Отображение может выступать в роли функции, возвращающей значение по ключу
(stringmap "a")          ; => 1
(keymap :a)              ; => 1

; При попытке получить отсутствующее значение, будет возвращён nil
(stringmap "d") ; => nil

; Иногда бывает удобно указать конкретное значение по-умолчанию:
({:a 1 :b 2} :c "Oops!") ; => "Oops!"

; Keywords тоже могут использоваться в роли функций!
(:b keymap) ; => 2

; Однако этот фокус не пройдёт со строками.
;("a" stringmap)
; => Exception: java.lang.String cannot be cast to clojure.lang.IFn

; Добавить пару ключ-значение в отображение можно функцией assoc
(def newkeymap (assoc keymap :d 4))
newkeymap ; => {:a 1, :b 2, :c 3, :d 4}

; Но всегда следует помнить, что значения в Clojure - неизменяемые!
keymap ; => {:a 1, :b 2, :c 3} - оригинал не был затронут

; dissoc позволяет исключить значение по ключу
(dissoc keymap :a :b) ; => {:c 3}

; Множества (Sets)
;;;;;;;;;;;;;;;;;;

(class #{1 2 3}) ; => clojure.lang.PersistentHashSet
(set [1 2 3 1 2 3 3 2 1 3 2 1]) ; => #{1 2 3}

; Добавляются элементы посредством conj
(conj #{1 2 3} 4) ; => #{1 2 3 4}

; Исключаются - посредством disj
(disj #{1 2 3} 1) ; => #{2 3}

; Вызов множества, как функции, позволяет проверить
; принадлежность элемента этому множеству:
(#{1 2 3} 1) ; => 1
(#{1 2 3} 4) ; => nil

; В пространстве имен clojure.sets
; содержится множество функций для работы с множествами

; Полезные формы
;;;;;;;;;;;;;;;;;

; Конструкции ветвления в clojure, это обычные макросы
; и подобны их собратьям в других языках:
(if false "a" "b") ; => "b"
(if false "a") ; => nil

; Специальная форма let позволяет присвоить имена значениям локально.
; При этом все изменения будут видны только вложенным формам:
(def a 10)
(let [a 1 b 2]
  (> a b)) ; => false

; Несколько форм можно объединить в одну форму посредством do
; Значением do-формы будет значение последней формы из списка вложенных в неё:
(do
  (print "Hello")
  "World") ; => "World" (prints "Hello")

; Множество макросов содержит внутри себя неявную do-форму.
; Пример - макрос определения функции:
(defn print-and-say-hello [name]
  (print "Saying hello to " name)
  (str "Hello " name))
(print-and-say-hello "Jeff") ;=> "Hello Jeff" (prints "Saying hello to Jeff")

; Ещё один пример - let:
(let [name "Urkel"]
  (print "Saying hello to " name)
  (str "Hello " name)) ; => "Hello Urkel" (prints "Saying hello to Urkel")

; Модули
;;;;;;;;;

; Форма "use" позволяет добавить в текущее пространство имен
; все имена (вместе со значениями) из указанного модуля:
(use 'clojure.set)

; Теперь нам доступны операции над множествами:
(intersection #{1 2 3} #{2 3 4}) ; => #{2 3}
(difference #{1 2 3} #{2 3 4}) ; => #{1}

; use позволяет указать, какие конкретно имена
; должны быть импортированы из модуля:
(use '[clojure.set :only [intersection]])

; Также модуль может быть импортирован формой require
(require 'clojure.string)

; После этого модуль становится доступе в текущем пространстве имен,
; а вызов его функций может быть осуществлен указанием полного имени функции:
(clojure.string/blank? "") ; => true

; Импортируемому модулю можно назначить короткое имя:
(require '[clojure.string :as str])
(str/replace "This is a test." #"[a-o]" str/upper-case) ; => "THIs Is A tEst."
; (Литерал вида #"" обозначает регулярное выражение)

; Вместо отдельной формы require (и use, хотя это и не приветствуется) можно
; указать необходимые модули прямо в форме ns:
(ns test
  (:require
    [clojure.string :as str] ; Внимание: при указании внутри формы ns
    [clojure.set :as set]))  ; имена пакетов не квотируются!

; Java
;;;;;;;

; Стандартная библиотека Java очень богата,
; и всё это богатство доступно и для Clojure!

; import позволяет импортировать модули Java
(import java.util.Date)

; В том числе и из ns
(ns test
  (:import java.util.Date
           java.util.Calendar))

; Имя класса, сопровождаемое символом "." позволяет
; инстанцировать объекты Java-классов:
(Date.) ; <a date object>

; форма . позволяет вызывать методы:
(. (Date.) getTime) ; <a timestamp>
(.getTime (Date.))  ; а можно и так

; Статические методы вызываются как функции модуля:
(System/currentTimeMillis) ; <a timestamp> (Модуль system всегда доступен!)

; doto позволяет удобно работать с объектами, изменяющими свое состояние
(import java.util.Calendar)
(doto (Calendar/getInstance)
  (.set 2000 1 1 0 0 0)
  .getTime) ; => A Date. set to 2000-01-01 00:00:00

; Работа с изменяемым сотоянием
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Clojure предоставляет набор инструментов
; для работы с изменяемым состоянием: Software Transactional Memory.
; Структуры STM представлены тремя типами:
; - атомы (atoms)
; - агенты (agents)
; - ссылки (references)

; Самые простые хранители состояния - атомы:
(def my-atom (atom {})) ; {} - начальное состояние атома

; Обновляется атом посредством swap!.
; swap! применяет функцию аргумент к текущему значению
; атома и помещает в атом результат
(swap! my-atom assoc :a 1) ; Обновляет my-atom, помещая в него (assoc {} :a 1)
(swap! my-atom assoc :b 2) ; Обновляет my-atom, помещая в него (assoc {:a 1} :b 2)

; Получить значение атома можно посредством '@'
; (провести так называемую операцию dereference)
my-atom  ;=> Atom<#...> (Возвращает объект типа Atom)
@my-atom ; => {:a 1 :b 2}

; Пример реализации счётчика на атоме
(def counter (atom 0))
(defn inc-counter []
  (swap! counter inc))

(inc-counter)
(inc-counter)
(inc-counter)
(inc-counter)
(inc-counter)

@counter ; => 5

; С другими STM-конструкциями - refs и agents - можно ознакомиться тут:
; Refs: http://clojure.org/refs
; Agents: http://clojure.org/agents
