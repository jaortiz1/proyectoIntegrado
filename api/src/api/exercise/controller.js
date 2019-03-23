import { success, notFound } from '../../services/response/'
import { Exercise } from '.'
const uploadService = require('../../services/upload/')

export const create = ({ bodymen: { body } }, res, next) =>
  Exercise.create(body)
    .then((exercise) => exercise.view(true))
    .then(success(res, 201))
    .catch(next)
/*uploadService.uploadFromBinary(req.file.buffer)
      .then((json) =>     
        Producto.create({
          nombre: req.body.nombre,
          codReferencia: req.body.codReferencia,
          descripcion: req.body.descripcion,
          dimensiones: req.body.dimensiones,
          distribuidor: req.body.distribuidor,
          categoria: req.body.categoria,
          foto: json.data.link
        })
      )
      .then((producto) => {
        productoCreado = producto;
        Categoria.findByIdAndUpdate(producto.categoria, { $push: {productos: producto}}).exec()
        return Distribuidor.findByIdAndUpdate(producto.distribuidor, { $push: {productos: producto}}).exec()
      })
    .then((distribuidor) => productoCreado.view(true))
      .then(success(res, 201))
      .catch(err => {
        console.log(err)
        next(err)
      })
    }*/

    /*export const create = (req, res, next) => {
// export const create = ({ bodymen: { body } }, res, next) => {
  uploadService.uploadFromBinary(req.file.buffer)
    .then(json => Photo.create({
      propertyId: req.body.propertyId,
      imgurLink: json.data.link,
      deletehash: json.data.deletehash
    }))
    .then((photo) => photo.view(true))
    .then(success(res, 201))
    .catch(next)
}*/
export const createWithPhoto = (req, res, next) => {
  uploadService.uploadFromBinary(req.file.buffer)
      .then((json) => 
        
        Exercise.create({
          name: req.body.name,
          series: req.body.series,
          repetitions: req.body.repetitions,
          finishTime: req.body.finishTime,
          restTime: req.body.restTime,
          gif: json.data.link,
          deletehash: json.data.deletehash,
          category: req.body.category,
          description: req.body.description,
          
        })
      )
      .then((exerciseCreated) => exerciseCreated.view(true))
      .then(success(res, 201))
      .catch(err => {
        console.log(err)
        next(err)
      })
    }

    
export const index = ({ querymen: { query, select, cursor } }, res, next) =>
  Exercise.count(query)
    
    .then(count => Exercise.find(query, select, cursor).populate('categoryId', 'name')
      .then((exercises) => ({
        count,
        rows: exercises.map((exercise) => exercise.view())
      }))
    )
    .then(success(res))
    .catch(next)

export const show = ({ params }, res, next) =>
  Exercise.findById(params.id)
    .populate('categoryId', 'name')
    .then(notFound(res))
    .then((exercise) => exercise ? exercise.view(true) : null)
    .then(success(res))
    .catch(next)

export const update = ({ bodymen: { body }, params }, res, next) =>
  Exercise.findById(params.id)
    .then(notFound(res))
    .then((exercise) => exercise ? Object.assign(exercise, body).save() : null)
    .then((exercise) => exercise ? exercise.view(true) : null)
    .then(success(res))
    .catch(next)
/*export const createWithPhoto = (req, res, next) => {
  uploadService.uploadFromBinary(req.file.buffer)
      .then((json) => 
        
        Exercise.create({
          name: req.body.name,
          series: req.body.series,
          repetitions: req.body.repetitions,
          finishTime: req.body.finishTime,
          restTime: req.body.restTime,
          gif: json.data.link,
          category: req.body.category,
          description: req.body.description,
          
        })
      )
      .then((exerciseCreated) => exerciseCreated.view(true))
      .then(success(res, 201))
      .catch(err => {
        console.log(err)
        next(err)
      })
    }*/

    /*Boleta.findOne({
  serie: req.params.id
})
.then((boleta) => {
  boleta.firma = req.params.firma;
  boleta
    .save()
    .then(() => {
      res.jsonp({ boleta }); // enviamos la boleta de vuelta
    });
});*/
/*Boleta.findOne({
  serie: req.params.id
})
.then((boleta) => {
  boleta.firma = req.params.firma;
  boleta
    .save()
    .then(() => {
      res.jsonp({ boleta }); // enviamos la boleta de vuelta
    });
});*/
export const updateWithPhoto = (req, res, next) => {
  
  uploadService.uploadFromBinary(req.file.buffer)
  Exercise.findById(req.params.id)
  .then(exercise=>{
    uploadService.uploadFromBinary(req.file.buffer)
    .then(json=>{
      console.log('NO MODIFICADO')
      console.log(exercise)
      exercise.gif=json.data.link;
      exercise.deletehash=json.data.deletehash;
      exercise.name=req.body.name;
      exercise.series=req.body.series;
      exercise.repetitions=req.body.repetitions;
      exercise.finishTime=req.body.finishTime;
      exercise.restTime=req.body.restTime;
      exercise.description=req.body.description;
      console.log('MODIFICADO')
      console.log(exercise)
      exercise
      .save()
      .then(() => {
        res.jsonp({ exercise }); // enviamos el ejercicio de vuelta
      });
    })

  }
  )
 
}

    



export const destroy = ({ params }, res, next) =>
  Exercise.findById(params.id)
    .then(notFound(res))
    .then((exercise) => exercise ? exercise.remove() : null)
    .then(success(res, 204))
    .catch(next)
