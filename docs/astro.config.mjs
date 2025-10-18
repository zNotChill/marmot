// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config

export default defineConfig({
	site: "https://marmot.znotchill.me",
	base: "/",
	integrations: [
		starlight({
			customCss: [
				"./src/styles/custom.css",
			],
			title: 'Marmot Developer API',
			social: [
				{ icon: 'github', label: 'GitHub', href: 'https://github.com/zNotChill/marmot' },
			],
			sidebar: [
				{
					label: 'Guides',
					items: [
						{ label: 'Get Started', slug: 'guides/get_started' },
						{
							label: 'Minestom',
							items: [
								{ label: 'Get Started', slug: 'guides/minestom/get_started' },
							],
						},
						{
							label: 'Paper',
							items: [
								{ label: 'Get Started', slug: 'guides/paper/get_started' },
							],
						},
						{
							label: 'API',
							items: [
								{ label: 'Keybinds', slug: 'guides/api/keybinds' },
								{
									label: 'UI',
									items: [
										{ label: 'Overview', slug: 'guides/api/ui/ui' },
										{ label: 'Animations', slug: 'guides/api/ui/animations' },
										{ label: 'Relative Positioning', slug: 'guides/api/ui/relative_positioning' },
										{
											label: 'Components',
											items: [
												{ label: 'Boxes', slug: 'guides/api/ui/components/boxes' },
												{ label: 'Gradients', slug: 'guides/api/ui/components/gradients' },
												{ label: 'Lines', slug: 'guides/api/ui/components/lines' },
												{ label: 'Sprites', slug: 'guides/api/ui/components/sprites' },
												{ label: 'Text', slug: 'guides/api/ui/components/text' },
											],
										},
									],
								},
								{ label: 'Camera Lock', slug: 'guides/api/camera_lock' },
								{ label: 'Camera', slug: 'guides/api/camera' },
								{ label: 'Camera Offset', slug: 'guides/api/camera_offset' },
								{ label: 'Marmot Player Data', slug: 'guides/api/marmot_player_data' },
								{ label: 'Perspective', slug: 'guides/api/perspective' },
							],
						},
					],
				},
				{
					label: 'Reference',
					autogenerate: { directory: 'reference' },
				},
			],
		}),
	],
});