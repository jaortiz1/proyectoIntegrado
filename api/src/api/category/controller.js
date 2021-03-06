import { success, notFound } from '../../services/response/'
import { Category } from '.'
export const create = ({ bodymen: { body } }, res, next) => {
  Category.create(body)
    .then((category) => category.view(true))
    .then(success(res, 201))
    .catch((err) => {
      if (err.name === 'MongoError' && err.code === 11000) {
        res.status(409).json({
          valid: false,
          param: 'name',
          message: 'category already registered'
        })
      } else {
        next(err)
      }
    })
    .catch(next)
}/*
export const create = ({ bodymen: { body } }, res, next) =>
  Category.create(body)
    .then((category) => {category.view(true)})
    .then(success(res, 201))
    //.catch(next)
    .catch((err) => {
      if (err.name === 'MongoError' && err.code === 11000) {
        res.status(409).json({
          valid: false,
          param: 'name',
          message: 'category already registered'
        })
      } else {
        next(err)
      }
    })
*/
export const index = ({ querymen: { query, select, cursor } }, res, next) =>
  Category.count(query)
    .then(count => Category.find(query, select, cursor)
      .then((categories) => ({
        count,
        rows: categories.map((category) => category.view())
      }))
    )
    .then(success(res))
    .catch(next)

export const show = ({ params }, res, next) =>
  Category.findById(params.id)
    .then(notFound(res))
    .then((category) => category ? category.view() : null)
    .then(success(res))
    .catch(next)

export const update = ({ bodymen: { body }, params }, res, next) =>
  Category.findById(params.id)
    .then(notFound(res))
    .then((category) => category ? Object.assign(category, body).save() : null)
    .then((category) => category ? category.view(true) : null)
    .then(success(res))
    .catch(next)

export const destroy = ({ params }, res, next) =>
  Category.findById(params.id)
    .then(notFound(res))
    .then((category) => category ? category.remove() : null)
    .then(success(res, 204))
    .catch(next)
/*
export const destroy = ({ params }, res, next) =>
  Category.findById(params.id)
    .then(notFound(res))
    .then((category=>{
      Exercise.find({categoryId: category.id})
      .then(exercisesFound=>{
        
        for (let i = 0; i < exercisesFound.length; i++) {
          const element = exercisesFound[i];
          console.log(element)
          element.categoryId="";
          
        }
      })
      
    }))
    .then((category) => category ? category.remove() : null)
    .then(success(res, 204))
    .catch(next)*/