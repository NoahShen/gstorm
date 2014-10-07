package gstorm.enhance

/**
 * Created by noahshen on 14-10-7.
 */
class DynamicFindEnhancer {

    List<String> parseTail(String name) {
        name.split('And(?=[A-Z])').findAll {
            it.length() > 0
        }.collect { String field ->
            if ( field.length() == 1) {
                return field.toLowerCase()
            }
            StringBuilder buf = new StringBuilder(field.size())
            buf.append(field.substring(0, 1).toLowerCase())
            buf.append(field.substring(1))
            return buf.toString()
        }
    }

    Map<String, Object> buildWhere(List<String> fields, def valuesOrValue) {
        Iterable values
        if (valuesOrValue instanceof Iterable) {
            values = valuesOrValue
        } else {
            values = [valuesOrValue]
        }
        Map<String, Object> where = [:]
        values.eachWithIndex { Object value, int i ->
            if (i < fields.size()) {
                String field = fields[i]
                where[field] = value
            }
        }
        return where
    }

    List getExtra(List fields, List args) {
        return args.size() > fields.size() ? args.subList(fields.size(), args.size()) : []
    }

    boolean supports(String method) {
        return method.startsWith('findBy') ||
                method.startsWith('findFirstBy') ||
                method.startsWith('findAllBy')
    }

    def tryExecute(Class model, String finder, List args) {
        String method
        String prefix
        if (finder.startsWith('findBy')) {
            method = 'where'
            prefix = 'findBy'
        }
//        else if (finder.startsWith('findFirstBy')) {
//            method = 'findFirstWhere'
//            prefix = 'findFirstBy'
//        }
        else if (finder.startsWith('findAllBy')) {
            method = 'where'
            prefix = 'findAllBy'
        } else {
            throw new MissingMethodException(finder, model, args)
        }
        String tail = finder.substring(prefix.length())
        List<String> fields = parseTail(tail)
        List values = args.subList(0, fields.size())
        Map query = buildWhere(fields, values)
//        List extra = getExtra(fields, args)
//        List newArgs = [query]
//        newArgs.addAll(extra)
//        model.metaClass.invokeStaticMethod(model, method, newArgs.toArray())

        String whereClause = query.inject('') { c, k, v ->
            c ? "$c and $k = '$v'" : "$k = '$v'"
        }
        model.metaClass.invokeStaticMethod(model, method, whereClause)
    }
}
