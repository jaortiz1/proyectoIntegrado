import { Router } from 'express'
import { middleware as query } from 'querymen'
import { middleware as body } from 'bodymen'
import { token, master } from '../../services/passport'
import { create, index, show, update, destroy } from './controller'
import { schema } from './model'
export Gym, { schema } from './model'

const router = new Router()
const { name, address, province, city, zipcode, position, price, description } = schema.tree

/**
 * @api {post} /gyms Create gym
 * @apiName CreateGym
 * @apiGroup Gym
 * @apiPermission admin
 * @apiParam {String} access_token admin access token.
 * @apiParam name Gym's name.
 * @apiParam address Gym's address.
 * @apiParam province Gym's province.
 * @apiParam city Gym's city.
 * @apiParam zipcode Gym's zipcode.
 * @apiParam position Gym's position.
 * @apiParam price Gym's price.
 * @apiParam description Gym's description.
 * @apiSuccess {Object} gym Gym's data.
 * @apiError {Object} 400 Some parameters may contain invalid values.
 * @apiError 404 Gym not found.
 * @apiError 401 admin access only.
 */
router.post('/',
  token({ required: true, roles: ['admin'] }),
  body({ name, address, province, city, zipcode, position, price, description }),
  create)

/**
 * @api {get} /gyms Retrieve gyms
 * @apiName RetrieveGyms
 * @apiGroup Gym
 * @apiPermission master
 * @apiParam {String} access_token master access token.
 * @apiUse listParams
 * @apiSuccess {Number} count Total amount of gyms.
 * @apiSuccess {Object[]} rows List of gyms.
 * @apiError {Object} 400 Some parameters may contain invalid values.
 * @apiError 401 master access only.
 */
router.get('/',
  master(),
  query(),
  index)

/**
 * @api {get} /gyms/:id Retrieve gym
 * @apiName RetrieveGym
 * @apiGroup Gym
 * @apiPermission master
 * @apiParam {String} access_token master access token.
 * @apiSuccess {Object} gym Gym's data.
 * @apiError {Object} 400 Some parameters may contain invalid values.
 * @apiError 404 Gym not found.
 * @apiError 401 master access only.
 */
router.get('/:id',
  master(),
  show)

/**
 * @api {put} /gyms/:id Update gym
 * @apiName UpdateGym
 * @apiGroup Gym
 * @apiPermission admin
 * @apiParam {String} access_token admin access token.
 * @apiParam name Gym's name.
 * @apiParam address Gym's address.
 * @apiParam province Gym's province.
 * @apiParam city Gym's city.
 * @apiParam zipcode Gym's zipcode.
 * @apiParam position Gym's position.
 * @apiParam price Gym's price.
 * @apiParam description Gym's description.
 * @apiSuccess {Object} gym Gym's data.
 * @apiError {Object} 400 Some parameters may contain invalid values.
 * @apiError 404 Gym not found.
 * @apiError 401 admin access only.
 */
router.put('/:id',
  token({ required: true, roles: ['admin'] }),
  body({ name, address, province, city, zipcode, position, price, description }),
  update)

/**
 * @api {delete} /gyms/:id Delete gym
 * @apiName DeleteGym
 * @apiGroup Gym
 * @apiPermission admin
 * @apiParam {String} access_token admin access token.
 * @apiSuccess (Success 204) 204 No Content.
 * @apiError 404 Gym not found.
 * @apiError 401 admin access only.
 */
router.delete('/:id',
  token({ required: true, roles: ['admin'] }),
  destroy)

export default router