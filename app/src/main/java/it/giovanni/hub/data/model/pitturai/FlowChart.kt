package it.giovanni.hub.data.model.pitturai

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class FlowChart(
    val last_node_id: Int,
    val last_link_id: Int,
    val nodes: List<Node>,
    val links: List<List<JsonElement>>,
    val groups: List<JsonElement>,
    val config: Map<String, JsonElement>,
    val extra: Map<String, JsonElement>,
    val version: Double
)

@Serializable
data class Node(
    val id: Int,
    val type: String,
    val pos: List<Int>,
    val size: Map<String, Double>,
    val flags: Map<String, JsonElement>,
    val order: Int,
    val mode: Int,
    val inputs: List<Input>? = null,
    val outputs: List<Output>? = null,
    val properties: Map<String, String>,
    val widgets_values: List<JsonElement>? = null // TODO: Sostituire JsonElement con String e modificare il json in modo che widgets_values nel json contenga solo stringhe?
)

@Serializable
data class Input(
    val name: String,
    val type: String,
    val link: Int
)

@Serializable
data class Output(
    val name: String,
    val type: String,
    val links: List<Int>,
    val slot_index: Int
)

fun main() {
    val jsonString1 = """{
        "last_node_id": 9,
        "last_link_id": 9,
        "nodes": [
            {
                "id": 7,
                "type": "CLIPTextEncode",
                "pos": [413, 389],
                "size": {"0": 425.27801513671875, "1": 180.6060791015625},
                "flags": {},
                "order": 3,
                "mode": 0,
                "inputs": [{"name": "clip", "type": "CLIP", "link": 5}],
                "outputs": [{"name": "CONDITIONING", "type": "CONDITIONING", "links": [6], "slot_index": 0}],
                "properties": {"node_name_for_sr": "CLIPTextEncode"},
                "widgets_values": ["text, watermark"]
            },
            {
                "id": 8,
                "type": "VAEDecode",
                "pos": [1209, 188],
                "size": {"0": 210, "1": 46},
                "flags": {},
                "order": 5,
                "mode": 0,
                "inputs": [
                    {"name": "samples", "type": "LATENT", "link": 7},
                    {"name": "vae", "type": "VAE", "link": 8}
                ],
                "outputs": [{"name": "IMAGE", "type": "IMAGE", "links": [9], "slot_index": 0}],
                "properties": {"node_name_for_sr": "VAEDecode"}
            },
            {
                "id": 9,
                "type": "SaveImage",
                "pos": [1451, 189],
                "size": {"0": 210, "1": 58},
                "flags": {},
                "order": 6,
                "mode": 0,
                "inputs": [{"name": "images", "type": "IMAGE", "link": 9}],
                "properties": {},
                "widgets_values": ["ComfyUI"]
            },
            {
                "id": 4,
                "type": "CheckpointLoaderSimple",
                "pos": [26, 474],
                "size": {"0": 315, "1": 98},
                "flags": {},
                "order": 0,
                "mode": 0,
                "outputs": [
                    {"name": "MODEL", "type": "MODEL", "links": [1], "slot_index": 0},
                    {"name": "CLIP", "type": "CLIP", "links": [3, 5], "slot_index": 1},
                    {"name": "VAE", "type": "VAE", "links": [8], "slot_index": 2}
                ],
                "properties": {"node_name_for_sr": "CheckpointLoaderSimple"},
                "widgets_values": ["Juggernaut_X_RunDiffusion_Hyper.safetensors"]
            },
            {
                "id": 6,
                "type": "CLIPTextEncode",
                "pos": [415, 186],
                "size": {"0": 422.84503173828125, "1": 164.31304931640625},
                "flags": {},
                "order": 2,
                "mode": 0,
                "inputs": [{"name": "clip", "type": "CLIP", "link": 3}],
                "outputs": [{"name": "CONDITIONING", "type": "CONDITIONING", "links": [4], "slot_index": 0}],
                "properties": {"node_name_for_sr": "CLIPTextEncode"},
                "widgets_values": ["beautiful scenery nature glass bottle landscape, , purple galaxy bottle,"]
            },
            {
                "id": 3,
                "type": "KSampler",
                "pos": [863, 186],
                "size": {"0": 315, "1": 262},
                "flags": {},
                "order": 4,
                "mode": 0,
                "inputs": [
                    {"name": "model", "type": "MODEL", "link": 1},
                    {"name": "positive", "type": "CONDITIONING", "link": 4},
                    {"name": "negative", "type": "CONDITIONING", "link": 6},
                    {"name": "latent_image", "type": "LATENT", "link": 2}
                ],
                "outputs": [{"name": "LATENT", "type": "LATENT", "links": [7], "slot_index": 0}],
                "properties": {"node_name_for_sr": "KSampler"},
                "widgets_values": [156680208700286, "randomize", 6, 1, "dpmpp_sde", "karras", 1]
            },
            {
                "id": 5,
                "type": "EmptyLatentImage",
                "pos": [473, 609],
                "size": {"0": 315, "1": 106},
                "flags": {},
                "order": 1,
                "mode": 0,
                "outputs": [{"name": "LATENT", "type": "LATENT", "links": [2], "slot_index": 0}],
                "properties": {"node_name_for_sr": "EmptyLatentImage"},
                "widgets_values": [768, 1024, 1]
            }
        ],
        "links": [
            [1, 4, 0, 3, 0, "MODEL"],
            [2, 5, 0, 3, 3, "LATENT"],
            [3, 4, 1, 6, 0, "CLIP"],
            [4, 6, 0, 3, 1, "CONDITIONING"],
            [5, 4, 1, 7, 0, "CLIP"],
            [6, 7, 0, 3, 2, "CONDITIONING"],
            [7, 3, 0, 8, 0, "LATENT"],
            [8, 4, 2, 8, 1, "VAE"],
            [9, 8, 0, 9, 0, "IMAGE"]
        ],
        "groups": [],
        "config": {},
        "extra": {},
        "version": 0.4
    }"""

    val json = Json { prettyPrint = true }

    // Decode
    val flowChart1: FlowChart = Json.decodeFromString<FlowChart>(jsonString1)
    println("flowChart1: $flowChart1")

    val flowChart2: FlowChart = json.decodeFromString<FlowChart>(jsonString1)
    println("flowChart2: $flowChart2")

    // Encode
    val jsonString2 = Json.encodeToString(flowChart1)
    val jsonString3 = json.encodeToString(flowChart1)
    println("jsonString1: $jsonString1")
    println("jsonString2: $jsonString2")
    println("jsonString3: $jsonString3")
}