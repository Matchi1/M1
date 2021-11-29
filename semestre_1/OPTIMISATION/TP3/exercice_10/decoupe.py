def decoupe(maximum, elements):
    element = elements[0]
    max_iter = int(maximum / element)
    if len(elements) == 1:
        return [[max_iter]]
    result = []
    for i in range(max_iter + 1):
        const = element * i
        values = decoupe(maximum - const, elements[1:])
        for elt in values:
            elt.insert(0, i)
            result.append(elt)
    return result

value = decoupe(300, [120, 100, 50])
print(len(value))
value = decoupe(500, [200, 120, 100, 50])
print(len(value))
